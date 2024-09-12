<?php
include('koneksi.php');
require 'vendor/autoload.php';
 
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Writer\Xlsx;
 
$spreadsheet = new Spreadsheet();
$sheet = $spreadsheet->getActiveSheet();
 
$sheet->setCellValue('A1', 'No');
$sheet->setCellValue('B1', 'NAMA LENGKAP');
$sheet->setCellValue('C1', 'ALAMAT');
$sheet->setCellValue('D1', 'JENIS KELAMIN');
$sheet->setCellValue('E1', 'EMAIL');
$sheet->setCellValue('F1', 'KONTAK');
 
$data = mysqli_query($koneksi,"select * from tbl_karyawan");
$i = 2;
$no = 1;
while($d = mysqli_fetch_array($data))
{
    $sheet->setCellValue('A'.$i, $no++);
    $sheet->setCellValue('B'.$i, $d['karyawan_nama']);
    $sheet->setCellValue('C'.$i, $d['karyawan_alamat']);
    $sheet->setCellValue('D'.$i, $d['karyawan_kelamin']);
    $sheet->setCellValue('E'.$i, $d['karyawan_email']);    
    $sheet->setCellValue('F'.$i, $d['karyawan_kontak']);    
    $i++;
}
 
$writer = new Xlsx($spreadsheet);
$writer->save('Data karyawan.xlsx');
echo "<script>window.location = 'Data karyawan.xlsx'</script>";
 
?>