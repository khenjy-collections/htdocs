<?php
defined('BASEPATH') or exit('No direct script access allowed');

include 'Omnitags.php';

class C_tabel_d3 extends Omnitags
{
	// Halaman publik


	// Halaman khusus akun


	// Halaman admin
	public function admin()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_d3_alias_v3_title'),
			'konten' => $this->v3['tabel_d3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_d3']),
			'tbl_d3' => $this->tl_d3->get_all_d3(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	public function tambah()
	{
		$this->declarew();
		$this->session_3();

		$data = array(
			$this->aliases['tabel_d3_field1'] => '',
			$this->aliases['tabel_d3_field2'] => userdata($this->aliases['tabel_c2_field1']),
			$this->aliases['tabel_d3_field3'] => date("Y-m-d\TH:i:s"),
			$this->aliases['tabel_d3_field4'] => date("Y-m-d\TH:i:s"),
		);

		$aksi = $this->tl_d3->insert_d3($data);
	}

	// Cetak semua data
	public function laporan()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_d3_alias_v4_title'),
			'konten' => $this->v4['tabel_d3'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_d3']),
			'tbl_d3' => $this->tl_d3->get_all_d3(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/printpage', $data);
	}

	// Cetak satu data
}
