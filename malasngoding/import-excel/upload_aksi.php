<?php 
include 'koneksi.php';
require 'vendor/autoload.php'; // Include Composer's autoloader

use PhpOffice\PhpSpreadsheet\IOFactory;

if(isset($_FILES['filepegawai'])){
    $file_name = $_FILES['filepegawai']['name'];
    $file_tmp = $_FILES['filepegawai']['tmp_name'];
    $file_type = $_FILES['filepegawai']['type'];

    // Validate file type, size, etc.

    $spreadsheet = IOFactory::load($file_tmp);
    $worksheet = $spreadsheet->getActiveSheet();
    $highestRow = $worksheet->getHighestRow();

    $berhasil = 0;

    for ($row = 2; $row <= $highestRow; $row++){
        $nama = $worksheet->getCellByColumnAndRow(1, $row)->getValue();
        $alamat = $worksheet->getCellByColumnAndRow(2, $row)->getValue();
        $telepon = $worksheet->getCellByColumnAndRow(3, $row)->getValue();

        if($nama != "" && $alamat != "" && $telepon != ""){
            $query = "INSERT INTO data_pegawai (nama, alamat, telepon) VALUES ('$nama', '$alamat', '$telepon')";
            if(mysqli_query($koneksi, $query)){
                $berhasil++;
            } else {
                // Handle insertion errors
            }
        }
    }

    unlink($file_tmp); // Delete uploaded file

    header("location:index.php?berhasil=$berhasil");
} else {
    // Handle file not uploaded error
}
?>
