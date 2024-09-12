<?php
defined('BASEPATH') or exit('No direct script access allowed');

include 'Omnitags.php';

class C_tabel_b9 extends Omnitags
{
	// Halaman publik
	public function detail($tabel_b9_field1 = NULL)
	{
		$this->declarew();
		$this->page_session_2_3_4_5();

		$tabel_b9 = $this->tl_b9->get_b9_by_b9_field1_by_b9_field2($tabel_b9_field1, userdata($this->aliases['tabel_c2_field1']))->result();

		if ($tabel_b9) {
			$tabel_b9_field2 = userdata($this->aliases['tabel_c2_field1']);

			// menggunakan nama khusus sama dengan konfigurasi
			$notif = array(
				$this->aliases['tabel_b9_field6'] => date("Y-m-d\TH:i:s"),
			);

			$aksi = $this->tl_b9->update_satu($notif, $tabel_b9_field1, $tabel_b9_field2);

			if ($aksi) {
				$data1 = array(
					'title' => lang('tabel_b9_alias_v8_title'),
					'konten' => $this->v8['tabel_b9'],
					'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b9']),
					'tbl_b9' => $this->tl_b9->get_b9_by_b9_field1($tabel_b9_field1),
				);

				$data = array_merge($data1, $this->package);

				if (get('refresh') !== 'true') {
					// Redirect to the same method with a refresh parameter
					load_view_data('_layouts/template', $data);
					redirect(current_url() . '?refresh=true');
				} else {
					load_view_data('_layouts/template', $data);
				}

			} else {
				redirect($_SERVER['HTTP_REFERER']);
			}
		} else {
			redirect(site_url($this->views['language'] . '/no_level'));
		}
	}

	public function lihat($tabel_b9_field1 = NULL)
	{
		$this->declarew();
		$this->session_2_3_4_5();

		$tabel_b9 = $this->tl_b9->get_b9_by_b9_field1_by_b9_field2($tabel_b9_field1, userdata($this->aliases['tabel_c2_field1']))->result();

		if ($tabel_b9) {

			$tabel_b9_field2 = userdata($this->aliases['tabel_c2_field1']);

			// menggunakan nama khusus sama dengan konfigurasi
			$notif = array(
				$this->aliases['tabel_b9_field6'] => date("Y-m-d\TH:i:s"),
			);

			$aksi = $this->tl_b9->update_satu($notif, $tabel_b9_field1, $tabel_b9_field2);
			redirect($_SERVER['HTTP_REFERER']);

		} else {
			redirect(site_url($this->views['language'] . '/invalid'));
		}
	}

	// Halaman khusus akun
	public function daftar()
	{
		$this->declarew();
		$this->page_session_2_3_4_5();

		$tabel_b9_field2 = userdata($this->aliases['tabel_c2_field1']);

		$data1 = array(
			'title' => lang('tabel_b9_alias_v2_title'),
			'konten' => $this->v2['tabel_b9'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b9']),
			'tbl_b9' => $this->tl_b9->get_b9_with_b8_by_b9_field2($tabel_b9_field2),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	// Halaman admin
	public function admin()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_b9_alias_v3_title'),
			'konten' => $this->v3['tabel_b9'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b9']),
			'tbl_b9' => $this->tl_b9->get_all_b9(),
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
			$this->aliases['tabel_b9_field1'] => '',
			$this->aliases['tabel_b9_field2'] => $this->v_post['tabel_b9_field2'],
			$this->aliases['tabel_b9_field6'] => htmlspecialchars($this->v_post['tabel_b9_field6']),
		);

		$aksi = $this->tl_b9->insert_b9($data);

		$notif = $this->handle_4b($aksi, 'tabel_b9');

		redirect($_SERVER['HTTP_REFERER']);
	}


	public function update()
	{
		$this->declarew();
		$this->session_2_3_4_5();

		$tabel_b9_field2 = userdata($this->aliases['tabel_c2_field1']);

		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_b9_field6'] => date("Y-m-d\TH:i:s"),
		);

		$aksi = $this->tl_b9->update_null($data, $tabel_b9_field2);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function delete($tabel_b9_field1 = null)
	{
		$this->declarew();
		$this->session_3();

		$tabel = $this->tl_b9->get_b1_by_b1_field1($tabel_b9_field1);
		$this->check_data($tabel);
		$aksi = $this->tl_b9->delete_b9($tabel_b9_field1);

		$notif = $this->handle_4e($aksi, 'tabel_b9', $tabel_b9_field1);

		redirect($_SERVER['HTTP_REFERER']);


	}

	// Cetak semua data
	public function laporan()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_b9_alias_v4_title'),
			'konten' => $this->v4['tabel_b9'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b9']),
			'tbl_b9' => $this->tl_b9->get_all_b9(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/printpage', $data);
	}

	// Cetak satu data
}
