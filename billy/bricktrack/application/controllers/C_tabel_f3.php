<?php
defined('BASEPATH') or exit('No direct script access allowed');

include 'Omnitags.php';

// Jujurly masih banyak bagian di controller ini yang masih menggunakan variabel biasa dan bukan menggunakan declare
// Aku juga ingin membuat sebuah fitur history transaksi dimana pesanan yang sudah masuk history bakal masuk ke sana


// Saat ini ketika data yang ada di tabel transaksi dan history, data-data yang berada di tabel transaksi bakal hilang
// Hal ini merupakan hal yang sedang aku coba teliti kepentingannya
// Aku perlu meneliti lebih jauh, ini adalah kedua pilihan yang kumiliki :
// 1. Menambahkan fitur untuk melihat data transksi saja, lalu diberi opsi apakah user ingin melihat data pesanan
// atau data history yang terhubung dengan data transaksi, jika perlu maka akan dicek data pesanan atau history tersebut.
// Jika data ada, maka akan ditampilkan, jika tidak akan muncul notifikasi data tidak ada
// 2. Opsi kedua adalah untuk membiarkannya tidak menampilkan data 

class C_tabel_f3 extends Omnitags
{
	// Halaman publik


	// Halaman khusus akun
	public function daftar()
	{
		$this->declarew();
		$this->page_session_4_5();

		$tabel_c2_field1 = userdata($this->aliases['tabel_c2_field1']);
		$data1 = array(
			'title' => lang('tabel_f3_alias_v2_title'),
			'konten' => $this->v2['tabel_f3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),
			'tbl_f3' => $this->tl_f3->get_f3_with_f2_with_e4_by_c2_field1($tabel_c2_field1),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	public function daftar_history()
	{
		$this->declarew();
		$this->session_2_4_5();

		$tabel_c2_field1 = userdata($this->aliases['tabel_c2_field1']);
		$data1 = array(
			'title' => lang('tabel_f3_alias_past'),
			'konten' => $this->views['tabel_f3_v2_alt'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),
			'tbl_f3' => $this->tl_f1->get_f1_with_f3_with_e4_by_c2_field1($tabel_c2_field1),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	// Halaman admin
	public function admin()
	{
		$this->declarew();
		$this->page_session_4();

		$data1 = array(
			'title' => lang('tabel_f3_alias_v3_title'),
			'konten' => $this->v3['tabel_f3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),
			'tbl_f3' => $this->tl_f3->get_all_f3(),
			'tbl_c1' => $this->tl_c1->get_all_c1(),

			// menggunakan nilai $min dan $max sebagai bagian dari $data
			// 'tgl_transaksi_min' => $param1,
			// 'tgl_transaksi_max' => $param2,
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}


	public function tambah()
	{
		// Masih membutuhkan kode untuk mencegah hal ini terjadi lebih dari satu kali dengan id tabel_f2 yang sama
		$this->declarew();
		$this->session_2_4_5();

		$tabel_f3_field2 = $this->v_post['tabel_f3_field2'];

		// seharusnya fitur ini menggunakan trigger cman saya tidak bisa melakukannya
		$tabel_f3_field7 = date("Y-m-d") . " " . date("h:m:s", time());

		// $kembalian = $this->tl_f2->get('harga_total') - $bayar;

		$data = array(
			$this->aliases['tabel_f3_field1'] => '',
			$this->aliases['tabel_f3_field2'] => $tabel_f3_field2,
			$this->aliases['tabel_f3_field3'] => $this->v_post['tabel_f3_field3'],
			$this->aliases['tabel_f3_field4'] => $this->v_post['tabel_f3_field4'],
			$this->aliases['tabel_f3_field5'] => $this->v_post['tabel_f3_field5'],
			$this->aliases['tabel_f3_field6'] => $this->aliases['tabel_f3_field6_value1'],
			$this->aliases['tabel_f3_field7'] => $tabel_f3_field7,
		);

		set_userdata($this->aliases['tabel_f3_field2'] . '_' . $this->aliases['tabel_f3'], $tabel_f3_field2, 30);

		$aksi = $this->tl_f3->insert_f3($data);

		$notif = $this->handle_4b($aksi, 'tabel_f3');

		redirect(site_url($this->language_code . '/' . $this->aliases['tabel_f3'] . '/konfirmasi'));
	}


	public function update()
	{
		$this->declarew();
		$this->session_2_4();

		$tabel_f3_field1 = $this->v_post['tabel_f3_field1'];

		$tabel_f3 = $this->tl_f3->get_f3_by_f3_field1($tabel_f3_field1)->result();
		$this->check_data($tabel_f3);

		// seharusnya fitur ini menggunakan trigger cman saya tidak bisa melakukannya
		$tabel_f3_field7 = date("Y-m-d\TH:i:s");

		$data = array(
			$this->aliases['tabel_f3_field5'] => $this->v_post['tabel_f3_field5'],
			$this->aliases['tabel_f3_field6'] => $this->v_post['tabel_f3_field6'],
			$this->aliases['tabel_f3_field7'] => $tabel_f3_field7,
		);

		$aksi = $this->tl_f3->update_f3($data, $tabel_f3_field1);

		$notif = $this->handle_4c($aksi, 'tabel_f3', $tabel_f3_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function delete($tabel_f3_field1 = null)
	{
		$this->declarew();
		$this->session_2_4();

		$tabel_f3 = $this->tl_f3->get_f3_by_f3_field1($tabel_f3_field1)->result();
		$this->check_data($tabel_f3);

		$aksi = $this->tl_f3->delete_f3($tabel_f3_field1);

		$notif = $this->handle_4e($aksi, 'tabel_f3', $tabel_f3_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	// Fitur filter untuk saat ini akan tidak digunakan terlebih dahulu
	public function filter()
	{
		$this->declarew();
		$this->page_session_4();

		// nilai min dan max sudah diinput sebelumnya
		$tabel_f3_field7_filter1 = $this->v_get['tabel_f3_field7_filter1'];
		$tabel_f3_field7_filter2 = $this->v_get['tabel_f3_field7_filter2'];

		$data1 = array(
			'title' => lang('tabel_f3_alias_v3_title'),
			'konten' => $this->v3['tabel_f3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),
			'tbl_f3' => $this->tl_f3->filter($tabel_f3_field7_filter1, $tabel_f3_field7_filter2),
			'tbl_f2' => $this->tl_f2->get_all_f3(),
			'tbl_e4' => $this->tl_e4->get_all_f3(),

			// menggunakan nilai $min dan $max sebagai bagian dari $data
			'tabel_f3_field7_filter1' => $tabel_f3_field7_filter1,
			'tabel_f3_field7_filter2' => $tabel_f3_field7_filter2,
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	// Cetak semua data
	public function laporan()
	{
		$this->declarew();
		$this->page_session_4();

		$data1 = array(
			'title' => lang('tabel_f3_alias_v4_title'),
			'konten' => $this->v4['tabel_f3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),
			'tbl_f3' => $this->tl_f3->get_all_f3(),
			'tbl_e4' => $this->tl_e4->get_all_f3(),
			'tbl_f2' => $this->tl_f2->get_all_f3(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/printpage', $data);
	}

	// Cetak satu data

	// Fitur print menurutku tidak memerlukan fitur join sama sekali 
	// karena sudah menggunakan parameter yang memilki nilai
	public function print($tabel_f3_field1 = null)
	{
		$this->declarew();
		$this->page_session_4_5();

		$param1 = $this->tl_f3->get_f3_by_f3_field1($tabel_f3_field1)->result();
		$this->check_data($param1);
		
		$data1 = array(
			'title' => lang('tabel_f3_alias_v5_title'),
			'konten' => $this->v5['tabel_f3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),
			'tbl_f3' => $this->tl_f3->get_f3_by_f3_field1($tabel_f3_field1)
		);

		$data = array_merge($data1, $this->package);
		
		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/printpage', $data);
	}

	// Fungsi khusus
	public function konfirmasi()
	{
		$this->declarew();
		$this->page_session_4();

		$tabel_f3_field2 = get_tempdata($this->aliases['tabel_f3_field2'] . '_' . $this->aliases['tabel_f3']);
		$data1 = array(
			'title' => lang('tabel_f3_alias_v4_title'),
			'konten' => $this->v7['tabel_f3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_f3']),

			// mengembalikan data baris terakhir/terbaru sesuai ketentuan dalam database untuk ditampilkan
			'tbl_f3' => $this->tl_f3->get_f3_by_f3_field2($tabel_f3_field2)->last_row(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/blank', $data);
	}

	public function approve($tabel_f3_field1 = null)
	{
		$this->declarew();
		$this->session_4();

		$tabel = $this->tl_f3->get_f3_by_f3_field1($tabel_f3_field1)->result();
		$this->check_data($tabel);

		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_f3_field6'] => $this->aliases['tabel_f3_field6_value2'],
		);

		$aksi = $this->tl_f3->update_f3($data, $tabel_f3_field1);

		$notif = $this->handle_4c($aksi, 'tabel_f3_field6', $tabel_f3_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function reject($tabel_f3_field1 = null)
	{
		$this->declarew();
		$this->session_4();

		$tabel = $this->tl_f3->get_f3_by_f3_field1($tabel_f3_field1)->result();
		$this->check_data($tabel);

		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_f3_field6'] => $this->aliases['tabel_f3_field6_value3'],
		);

		$aksi = $this->tl_f3->update_f3($data, $tabel_f3_field1);

		$notif = $this->handle_4c($aksi, 'tabel_f3_field6', $tabel_f3_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}
}
