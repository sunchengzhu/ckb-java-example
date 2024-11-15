import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.InputIterator;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws IOException {
        CkbRpcApi ckbApi = new Api("https://testnet.ckb.dev/");

        // https://github.com/ckb-devrel/offckb/blob/d10b42920463068db4eb757b23a82de7eaf63476/account/account.json#L47-L67
        String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqvkvhntcxtxas4lejj0z9uz62usduuy8rcwg4fr9";
        String receiver = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqgdl92j7574rgmc4w3x00y93kk6g3lggqq23mmmd";
        Iterator<TransactionInput> iterator = new InputIterator(sender);
        TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(Network.TESTNET);
        configuration.setFeeRate(1000);
        TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
                .addOutput(receiver, 50100000000L)
                .setChangeOutput(sender)
                .build();
        // 0. Set your private key
        String privateKey = "0x0334ddff3b1e19af5c5fddda8dbcfb235416eaaba11cfca8acf63ad46e9f55b2";
        // 1. Sign transaction with your private key
        TransactionSigner.getInstance(Network.TESTNET).signTransaction(txWithGroups, privateKey);
        // 2. Send transaction to CKB node
        byte[] txHash = ckbApi.sendTransaction(txWithGroups.txView);
        System.out.println(Numeric.toHexString(txHash));
    }
}