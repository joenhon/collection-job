import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.collection.entity.Account;
import org.collection.entity.UTXO;
import org.collection.service.FuUserWalletService;
import org.collection.util.HttpUtil;
import org.collection.util.Usdt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml" })
public class MyTest {

    @Autowired
    private FuUserWalletService fuUserWalletService;

    @Test
    public void Test1(){
        System.out.println("s");
        System.out.println(Usdt.getFee());
        //System.out.println(fuUserWalletService.selectWallet(0).get(0).getAddress());
        /*Utxo utxo1=new Utxo();
        utxo1.setTxid("836b743d4101dcec9a7d7d5794d09e7abc66b42d17a666835e102ca928c6d954");
        utxo1.setVout((long) 2);

        Utxo utxo2=new Utxo();
        utxo2.setTxid("ac168c260718fa800b08ab734f01af4c3a592e1ca7df6f2a97c7caca5c4e40fc");
        utxo2.setVout((long) 0);

        System.out.println(USDT.createRawTransaction(utxo1,utxo2));*/
        /*Thread thread=new CollectionJob(fuUserWalletService);
        thread.start();
        try {
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }
}
