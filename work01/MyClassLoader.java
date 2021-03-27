package homework.work01;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author liqicong@myhexin.com
 * @date 2021/3/26 16:00
 */
public class MyClassLoader extends ClassLoader{
    public static void main(String[] args){
        MyClassLoader myClassLoader=new MyClassLoader();
        try{

            Class<?> log=myClassLoader.loadClass("Hello");
            System.out.println("类加载器是"+log.getClassLoader());

            Method method=log.getDeclaredMethod("hello");
            method.invoke(log.newInstance());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        String base64="yv66vgAAADQAHwoABgARCQASABMIABQKABUAFgcAFwcAGAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAVTHN1aWJpYW54aWV4aWUvSGVsbG87AQAIPGNsaW5pdD4BAApTb3VyY2VGaWxlAQAKSGVsbG8uamF2YQwABwAIBwAZDAAaABsBABFoZWxsbyBteSBwbGVhc3VyZQcAHAwAHQAeAQATc3VpYmlhbnhpZXhpZS9IZWxsbwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYAIQAFAAYAAAAAAAIAAQAHAAgAAQAJAAAALwABAAEAAAAFKrcAAbEAAAACAAoAAAAGAAEAAAAHAAsAAAAMAAEAAAAFAAwADQAAAAgADgAIAAEACQAAACUAAgAAAAAACbIAAhIDtgAEsQAAAAEACgAAAAoAAgAAAAoACAALAAEADwAAAAIAEA==";
//        byte[] bytes=decode(base64);
        byte[] readAllBytes=null;
        try {
            readAllBytes = Files.readAllBytes(new File("D:\\360安全浏览器下载\\Hello.xlass\\Hello.xlass").toPath());
            for(int i=0;i<readAllBytes.length;i++){
                readAllBytes[i]=(byte)(255-(readAllBytes[i]));
            }

            System.out.println(readAllBytes.length);
//            bytes=readAllBytes;
        }catch (Exception e){
            e.printStackTrace();
        }

        return defineClass(name,readAllBytes,0,readAllBytes.length);
    }


    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    public byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }
}
