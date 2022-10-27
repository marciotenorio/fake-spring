package business;

public class FakeLogger {

    public void beforeRemoteInvoke(String str){
        System.out.println(str);
    }
}
