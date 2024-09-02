package crud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operasi {

    public static void tampilkanData() throws IOException {

        FileReader fileInput;
        BufferedReader bufferInput;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e){
            System.err.println("database tidak ditemukan");
            System.err.println("silahkan tambah data terlebih dahulu");
            return;
        }


        System.out.println("\n|No |\tTahun |\tPenulis                 |\tPenerbit                |\t Judul Buku");
        System.out.println("---------------------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        int noData = 0;
        while (data != null){
            noData++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            stringToken.nextToken();
            System.out.printf("| %2d",noData);
            System.out.printf("|\t%4s  ",stringToken.nextToken());
            System.out.printf("|\t%-20s     ",stringToken.nextToken());
            System.out.printf("|\t%-20s    ",stringToken.nextToken());
            System.out.printf("|\t%s     \n",stringToken.nextToken());

            data = bufferInput.readLine();
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        bufferInput.close();
        fileInput.close();

    }

    public static void cariData() throws IOException {

        // membaca database ada atau tidak
        try {
            File file = new File ("database.txt");
        } catch (Exception e){
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambahkan database terlebih dahulu");
            tambahData();
            return;
        }
        // ambil keyword dari user
        Scanner userOption = new Scanner(System.in);
        System.out.println("Masukkan kata kunci untuk mencari buku:");

        String dataString = userOption.nextLine();

        String[] keywords = dataString.split("\\s+");

        // cek keyword di database
        Utility.cekBukuDatabase(keywords,true);

    }

    public static void updateData () throws IOException {
        //ambil database original
        File database = new File ("database.txt");
        FileReader fileInput = new FileReader (database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // buat database sementara
        File tempDB = new File ("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
       System.out.println("List Buku: ");
       tampilkanData();

       // ambil user input untuk delete data
        Scanner inputData = new Scanner (System.in);
        System.out.println("\nMasukkan nomor buku yang akan diedit: ");
        int editNum = inputData.nextInt();

        int entryCounts = 0; 
        String data = bufferedInput.readLine();

        // Looping untuk membaca tiap data baris dan skip data yang akan di delete
        while (data != null) {
            entryCounts++;
          
    
            StringTokenizer stringToken = new StringTokenizer(data, ",");
            // tampilkan data yang ingin dihapus
    
    
            if (editNum == entryCounts){
                System.out.println("\nData yang ingin anda edit adalah: ");
                System.out.println("----------------------------------------");
                System.out.println("Referensi :          " + stringToken.nextToken());
                System.out.println("Tahun     :          " + stringToken.nextToken());
                System.out.println("Penulis   :          " + stringToken.nextToken());
                System.out.println("Penerbit  :          " + stringToken.nextToken());
                System.out.println("Judul     :          " + stringToken.nextToken());

                // update data

                // mengambil input dari user
                String[] fieldData = {"tahun","penulis","penerbit","judul"};
                String[] tempData = new String[4];
                
                // refresh token
                stringToken = new StringTokenizer(data, ",");
                String originalData = stringToken.nextToken();
                for (int i = 0; i < fieldData.length; i++) {
                boolean isUpdate =  Utility.getYesorNo("apakah anda ingin merubah data " + fieldData[i]);


                originalData = stringToken.nextToken();
                if (isUpdate){
                    // user input
                    inputData = new Scanner (System.in);
                    System.out.print("\nMasukkan " + fieldData[i] + " baru: ");
                    tempData[i] = inputData.nextLine();
                } else {
                    tempData[i] = originalData;
                }
                
            }

            System.out.println(Arrays.toString(tempData));

            // tampilkan data baru ke layar
            stringToken = new StringTokenizer(data, ",");
            stringToken.nextToken();
            System.out.println("\nData baru Anda adalah: ");
                System.out.println("----------------------------------------");
                System.out.println("Tahun     :          " + stringToken.nextToken() + " --> " + tempData[0]);
                System.out.println("Penulis   :          " + stringToken.nextToken() + " --> " + tempData[1]);
                System.out.println("Penerbit  :          " + stringToken.nextToken() + " --> " + tempData[2]);
                System.out.println("Judul     :          " + stringToken.nextToken() + " --> " + tempData[3]);

             boolean isUpdate = Utility.getYesorNo("apakah anda yakin ingin mengupdate data tersebut? ");

             if (isUpdate){

                // cek data baru di database
                boolean isExist = Utility.cekBukuDatabase(tempData, false);

            if (isExist){
                System.err.println("data buku di database sudah ada, proses update data dibatalkan \nsilahkan edit ulang");
            } else {

                // format data baru ke dalam database
                String tahun = tempData[0];
                String penulis = tempData[1];
                String penerbit = tempData[2];
                String judul = tempData[3];

                // buat primary key
                long nomorEntry = 1;
                String penulisTanpaSpasi = penulis.replaceAll("\\s+","");
                String primaryKey = penulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;
                String dataBaru = primaryKey+","+tahun+","+ penulis+","+ penerbit+","+judul;   
                // tulis data ke database
                bufferedOutput.write(dataBaru);
              


            }   

             } else {
                  // copy data 
                  bufferedOutput.write(data);
                  

             }


            } else {
                // copy data 
                bufferedOutput.write(data);
                
            }
            bufferedOutput.newLine();
            data = bufferedInput.readLine();
        } 

        // menulis data ke file
      bufferedOutput.flush();
      fileInput.close();
      bufferedInput.close();
      fileOutput.close();
      bufferedOutput.close();
      System.gc();

      
      // delete original file
      database.delete();

      // rename file sementara ke database
      try {
       File file = new File("tempDB.txt");
       file.renameTo(new File("database.txt"));
      } catch (Exception e){
       System.out.println("I/O Error");
      }
        
       
    } 

    public static void deleteData () throws IOException {
        // ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

       // buat database sementara
       File tempDB = new File("tempDB.txt");
       FileWriter fileOutput = new FileWriter(tempDB);
       BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

       // tampilkan data
       System.out.println("List Buku: ");
       tampilkanData();

       // ambil user input untuk delete data
       Scanner inputData = new Scanner (System.in);
       System.out.print("\nMasukkan nomor buku yang akan dihapus: ");
       int deleteNum = inputData.nextInt();

       

       // looping untuk membaca tiap data baris dan skip data yg akan di delete

      
       int entryCounts = 0;

       String data = bufferedInput.readLine();

       while (data != null) {
        entryCounts++;
        boolean isDelete = false;

        StringTokenizer stringToken = new StringTokenizer(data,",");
        // tampilkan data yang ingin dihapus


        if (deleteNum == entryCounts){
            System.out.println("\nData yang ingin anda hapus adalah: ");
            System.out.println("----------------------------------------");
            System.out.println("Referensi :          " + stringToken.nextToken());
            System.out.println("Tahun     :          " + stringToken.nextToken());
            System.out.println("Penulis   :          " + stringToken.nextToken());
            System.out.println("Penerbit  :          " + stringToken.nextToken());
            System.out.println("Judul     :          " + stringToken.nextToken());

            isDelete = Utility.getYesorNo("Apakah anda yakin ingin menghapus data? ");
        } 

        if(isDelete){
            // skip pindahkan data dari original ke sementara
            System.out.println("data berhasil dihapus");
        } else {
            // pindahkan data dari original ke sementara
            bufferedOutput.write(data);
            bufferedOutput.newLine();
        }
        data = bufferedInput.readLine();

       }
    
       if (deleteNum > entryCounts){
        System.err.println("data yang anda masukkan tidak ada\nsilahkan masukkan data lain!");
        
       }
       // menulis data ke file
       bufferedOutput.flush();
       fileInput.close();
       bufferedInput.close();
       fileOutput.close();
       bufferedOutput.close();
       System.gc();

       
       // delete original file
       database.delete();

       // rename file sementara ke database
       try {
        File file = new File("tempDB.txt");
        file.renameTo(new File("database.txt"));
       } catch (Exception e){
        System.out.println("I/O Error");
       }

     
    }

    public static void tambahData() throws IOException {
        FileWriter fileOutput = new FileWriter("database.txt", true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);
    
        // mengambil input dari user
        Scanner inputData = new Scanner(System.in);
        System.out.println("Masukkan data buku!");
        String penulis, judul, penerbit, tahun;
    
        System.out.print("masukkan nama penulis: ");
        penulis = inputData.nextLine();
        System.out.print("masukkan judul buku: ");
        judul = inputData.nextLine();
        System.out.print("masukkan nama penerbit: ");
        penerbit = inputData.nextLine();
        System.out.print("masukkan tahun terbit: ");
        tahun = Utility.ambilTahun();
    
        // cek buku di database
    
        String[] keywords = {tahun + "," + penulis + "," + penerbit + "," + judul};
        System.out.println(Arrays.toString(keywords));
    
        boolean isExist = Utility.cekBukuDatabase(keywords, false);
    
        // menulis buku di database
    
        if (!isExist){
    //        fiersabesari_2012_1,2012,fiersa besari,media kita,jejak langkah
            long nomorEntry = 1;
            String penulisTanpaSpasi = penulis.replaceAll("\\s+","");
            String primaryKey = penulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;
            System.out.println("\nData yang akan anda masukkan adalah");
            System.out.println("--------------------------------------");
            System.out.println("primary key  = " + primaryKey);
            System.out.println("tahun terbit = " + tahun);
            System.out.println("penulis      = " + penulis);
            System.out.println("judul        = " + judul);
            System.out.println("penerbit     = " + penerbit);
            String dataBaru = primaryKey+","+tahun+","+ penulis+","+ penerbit+","+judul;     
    
            boolean isTambah = Utility.getYesorNo("Apakah anda ingin menambah data tersebut? ");
    
            if (isTambah) {
                bufferOutput.write(dataBaru);
                bufferOutput.newLine();
                bufferOutput.flush();
                
            }
    
        } else {
            System.out.println("buku yang anda akan anda masukkan sudah tercantum di database dengan data berikut:");
            Utility.cekBukuDatabase(keywords, true);
        }
    
    
        
    
      
    
        bufferOutput.close();
    
    
    
        }

    
}
