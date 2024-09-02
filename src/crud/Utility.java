package crud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {
    
    // default access modifier
    static boolean cekBukuDatabase (String[] keywords, boolean isDisplay) throws IOException{
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist = false;
        int jumlahData = 0;

        if (isDisplay) {
            System.out.println("\n|No |\tTahun |\tPenulis                 |\tPenerbit                |\t Judul Buku");
            System.out.println("---------------------------------------------------------------------------------------");
        }
        while (data != null){

            // cek keywords di dalam baris
            isExist = true;

//            System.out.println(data);
//            System.out.println(Arrays.toString(keywords));
            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());

            }


//            System.out.println(isExist);

            // jika keyword cocok maka tampilkan

            if (isExist){
                if (isDisplay){
                    jumlahData++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d",jumlahData);
                    System.out.printf("|\t%4s  ",stringToken.nextToken());
                    System.out.printf("|\t%-20s     ",stringToken.nextToken());
                    System.out.printf("|\t%-20s    ",stringToken.nextToken());
                    System.out.printf("|\t%s     \n",stringToken.nextToken());
                } else {
                    break;
                }


            }

            data = bufferInput.readLine();
        }

        if(isDisplay) {
            System.out.println("---------------------------------------------------------------------------------------");
        }

        bufferInput.close();
        fileInput.close();
        return isExist;
    }

    protected static String ambilTahun () throws IOException {
        boolean tahunValid = false;
        Scanner inputData = new Scanner(System.in);
        String tahunInput = inputData.nextLine();


        while (!tahunValid){
            try {
                Year.parse(tahunInput);
                tahunValid = true;
            } catch (Exception e){
                System.err.println("\ntahun tidak valid, \nsilahkan masukkan data yang sesuai (format = YYYY)");
                System.out.print("masukkan tahun lagi: ");
                tahunInput = inputData.nextLine();
            }

        }

        return tahunInput;
    }

    public static boolean getYesorNo (String message){
        Scanner userOption = new Scanner(System.in);
        System.out.print("\n" + message + "(y/n)?");
        String pilihanUser = userOption.next();

        while (!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")){
            System.err.println("pilihan anda bukan y/n \nsilahkan pilih y/n");
            System.out.print("\n" + message + "(y/n)?");
            pilihanUser = userOption.next();
        }

        return pilihanUser.equalsIgnoreCase("y");
    }


    public static void clearScreen(){
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e){
            System.err.println("tidak bisa clear screen");
        }
    }

}
