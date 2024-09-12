<?php
defined('BASEPATH') or exit('No direct script access allowed');

include 'Omnitags.php';

class C_tabel_b8 extends Omnitags
{
	// Halaman publik


	// Halaman khusus akun


	// Halaman admin
	public function admin()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_b8_alias_v3_title'),
			'konten' => $this->v3['tabel_b8'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b8']),
			'tbl_b8' => $this->tl_b8->get_all_b8(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	public function tambah()
	{
		$this->declarew();
		$this->session_3();

		validate_input(
			array(
				$this->v_post['tabel_b8_field2'],
				$this->v_post['tabel_b8_field3'],
				$this->v_post['tabel_b8_field4'],
			),
			$this->views['flash2'],
			'tambah'
		);

		$data = array(
			$this->aliases['tabel_b8_field1'] => '',
			$this->aliases['tabel_b8_field2'] => $this->v_post['tabel_b8_field2'],
			$this->aliases['tabel_b8_field3'] => $this->v_post['tabel_b8_field3'],
			$this->aliases['tabel_b8_field4'] => $this->v_post['tabel_b8_field4'],
		);

		$aksi = $this->tl_b8->insert_b8($data);

		$notif = $this->handle_4b($aksi, 'tabel_b8');

		redirect($_SERVER['HTTP_REFERER']);
	}


	public function update() //update tidak diperlukan di sini
	{
		$this->declarew();
		$this->session_3();

		$tabel_b8_field1 = $this->v_post['tabel_b8_field1'];

		$tabel_b8 = $this->tl_b8->get_b8_by_b8_field1($tabel_b8_field1)->result();
		$this->check_data($tabel_b8);

		validate_input(
			array(
				$this->v_post['tabel_b8_field1'],
				$this->v_post['tabel_b8_field3'],
				$this->v_post['tabel_b8_field4'],
			),
			$this->views['flash3'],
			'ubah' . $tabel_b8_field1
		);


		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_b8_field3'] => $this->v_post['tabel_b8_field3'],
			$this->aliases['tabel_b8_field4'] => $this->v_post['tabel_b8_field4'],
		);

		$aksi = $this->tl_b8->update_b8($data, $tabel_b8_field1);

		$notif = $this->handle_4c($aksi, 'tabel_a1', $tabel_b8_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}


	public function delete($tabel_b8_field1 = null)
	{
		$this->declarew();
		$this->session_3();

		$tabel_b8 = $this->tl_b8->get_b8_by_b8_field1($tabel_b8_field1)->result();
		$this->check_data($tabel_b8);

		$aksi = $this->tl_b8->delete_b8($tabel_b8_field1);

		$notif = $this->handle_4e($aksi, 'tabel_b8', $tabel_b8_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	// Cetak semua data
	public function laporan()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_b8_alias_v4_title'),
			'konten' => $this->v4['tabel_b8'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b8']),
			'tbl_b8' => $this->tl_b8->get_all_b8(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/printpage', $data);
	}

	// Cetak satu data
}
