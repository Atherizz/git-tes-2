package com.tutorial;

import java.io.IOException;
import java.util.Scanner;

import crud.Operasi;
import crud.Utility;


public class Main {

    public static void main(String[] args) throws IOException {


        Scanner userOption = new Scanner(System.in);
        String pilihanUser;
        boolean isLanjutkan = true;

        while (isLanjutkan) {
            Utility.clearScreen();
            System.out.println("DATABASE PERSPUSTAKAAN \n");
            System.out.println("1.\tLihat Seluruh Buku");
            System.out.println("2.\tCari Data Buku");
            System.out.println("3.\tTambah Data Buku");
            System.out.println("4.\tUbah Data Buku");
            System.out.println("5.\tHapus Data Buku");


            System.out.print("\n\npilihan Anda: " );
            pilihanUser = userOption.next();

            switch (pilihanUser) {
                case "1":
                    System.out.println("\n=================");
                    System.out.println("LIST SELURUH BUKU");
                    System.out.println("=================");
                    Operasi.tampilkanData();
                    break;
                case "2":
                    System.out.println("\n==========");
                    System.out.println("CARI BUKU");
                    System.out.println("==============");
                    Operasi.cariData();
                    break;
                case "3":
                    System.out.println("\n===============");
                    System.out.println("TAMBAH DATA BUKU");
                    System.out.println("================");
                    Operasi.tambahData();
                    break;
                case "4":
                    System.out.println("\n=============");
                    System.out.println("UBAH DATA BUKU");
                    System.out.println("=============");
                    Operasi.updateData();
                    break;
                case "5":
                    System.out.println("\n=============");
                    System.out.println("HAPUS DATA BUKU");
                    System.out.println("=============");
                    Operasi.deleteData();
                    break;
                default:
                    System.err.println ("\n input tidak ditemukan\n silahkan pilih 1-5");


            }


            isLanjutkan = Utility.getYesorNo("apakah Anda ingin melanjutkan");
        }

    }

}
