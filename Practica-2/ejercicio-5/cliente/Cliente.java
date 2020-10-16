
/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/
import java.rmi.Naming; /* lookup */
import java.rmi.RemoteException;
import java.rmi.registry.Registry; /* REGISTRY_PORT */
import java.sql.Timestamp;

public class Cliente {

    private static IfaceRemoteClass remote;

    private static double calcularDE(long numArray[])
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(long num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(long num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    private static void testTime() {
        int sampleSize=10000;
        long start, end, diff;
        long[] samples = new long[sampleSize];
        double total = 0L;
        try {
            for (int i = 0; i < sampleSize; i++) {
                start = System.currentTimeMillis();
                remote.metodoInutil();
                end = System.currentTimeMillis();
                diff = end - start;
                samples[i] =diff;
                total = total + diff;  
                //System.out.println(start);
            }
            final double std = calcularDE(samples);
            System.out.println("Total: " + total + "ms");
            
            System.out.println("Promedio: " + total / sampleSize + "ms");
            System.out.println("Desviacion estandar: " + std + "ms");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
        try {
            remote = (IfaceRemoteClass) Naming.lookup(rname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testTime();
    }
}