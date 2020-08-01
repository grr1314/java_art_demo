package section3.LockDemo;

public class Demo {
    public static void main(String[] args) {
        ReorderExample eReorderExample=new ReorderExample();
        // new Thread(new Runnable(){
        //     @Override
        //     public void run() {
                eReorderExample.writer();
        //     }
           
        // }).start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                eReorderExample.reader();
            }
           
        }).start();
    }
    
}