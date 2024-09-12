<div class="row mb-2 align-items-center">
  <div class="col-md-9 d-flex align-items-center">
    <h1><?= $title ?><?= count_data($tbl_f2) ?><?= $phase ?></h1>
  </div>
  <div class="col-md-3 text-right">
    <?php foreach ($dekor->result() as $dk): ?>
      <img src="img/<?= $tabel_b1 ?>/<?= $dk->$tabel_b1_field4 ?>" width="200" alt="Image">
    <?php endforeach ?>
  </div>
</div>
<hr>




<div class="row">
  <div class="col-md-10">
    <?= btn_tambah() ?>
  </div>

  <div class="col-md-2 d-flex justify-content-end">
    <?= view_switcher() ?>
  </div>
</div>

<div id="card-view" class="row data-view active">
  <?php foreach ($tbl_f2->result() as $tl_f2):
    echo card_regular(
      $tl_f2->$tabel_f2_field1,
      $tl_f2->$tabel_f2_field1,
      card_content('tabel_f2_field2', $tl_f2->$tabel_f2_field2) .
      card_content('tabel_f2_field3', $tl_f2->$tabel_f2_field3) .
      card_content('tabel_f2_field4', $tl_f2->$tabel_f2_field4) .
      card_content('tabel_f2_field5', $tl_f2->$tabel_f2_field5) .
      card_content('tabel_f2_field6', $tl_f2->$tabel_f2_field6),
      btn_edit($tl_f2->$tabel_f2_field1) . ' ' .
      btn_print('tabel_f2', $tl_f2->$tabel_f2_field1),
      'text-light bg-danger',
      'col-md-4',
      $tabel_f2,
    );
  endforeach; ?>
</div>

<div id="table-view" class="table-responsive data-view" style="display: none;">
  <table class="table table-light" id="data">
    <thead class="thead-light">
      <tr>
        <th><?= lang('no') ?></th>
        <th><?= lang('tabel_f2_field1_alias') ?></th>
        <th><?= lang('tabel_f2_field2_alias') ?></th>
        <th><?= lang('tabel_f2_field3_alias') ?></th>
        <th><?= lang('tabel_f2_field4_alias') ?></th>
        <th><?= lang('tabel_f2_field5_alias') ?></th>
        <th><?= lang('tabel_f2_field6_alias') ?></th>
        <th><?= lang('action') ?></th>
      </tr>
    </thead>

    <tbody>
      <?php foreach ($tbl_f2->result() as $tl_f2): ?>
        <tr>
          <td></td>
          <td><?= $tl_f2->$tabel_f2_field1 ?></td>
          <td><?= $tl_f2->$tabel_f2_field2 ?></td>
          <td><?= $tl_f2->$tabel_f2_field3 ?></td>
          <td><?= $tl_f2->$tabel_f2_field4 ?></td>
          <td><?= $tl_f2->$tabel_f2_field5 ?></td>
          <td><?= $tl_f2->$tabel_f2_field6 ?></td>
          <td>
            <?= btn_edit($tl_f2->$tabel_f2_field1) ?>
            <?= btn_print('tabel_f2', $tl_f2->$tabel_f2_field1) ?>
            <?= btn_hapus('tabel_f2', $tl_f2->$tabel_f2_field1) ?>

          </td>

        </tr>
      <?php endforeach ?>
    </tbody>

  </table>
</div>

<!-- modal tambah -->
<div id="tambah" class="modal fade tambah">
  <div class="modal-dialog">
    <div class="modal-content">
      <?= modal_header_add(lang('add') . ' ' . lang('tabel_f2_alias'), '') ?>
      <form action="<?= site_url($language . '/' . $tabel_f2 . '/tambah') ?>" method="post">
        <div class="modal-body">
          <div class="form-group">
            <select class="form-control float" required name="<?= $tabel_f2_field2_input ?>"
              id="<?= $tabel_f2_field2_input ?>">
              <?php foreach ($tbl_c1->result() as $tl_c1): ?>
                <option value="<?= $tl_c1->$tabel_c1_field1 ?>"><?= $tl_c1->$tabel_c1_field2 ?></option>
              <?php endforeach ?>
            </select>
            <label for="<?= $tabel_f2_field2_input ?>" class="form-label"><?= lang('select') ?>
              <?= $tabel_f2_field2_alias ?></label>
          </div>
          <?= add_min_max('date', 'tabel_f2_field3', 'required', date("Y-m-d"), '') ?>
          <?= add_min_max('time', 'tabel_f2_field4', 'required', '', '') ?>
        </div>
        <!-- memunculkan notifikasi modal -->
        <p class="small text-center text-danger"><?= get_flashdata('pesan_tambah') ?></p>
        <div class="modal-footer">
          <?= btn_simpan() ?>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- modal ubah -->
<?php foreach ($tbl_f2->result() as $tl_f2): ?>
  <div id="ubah<?= $tl_f2->$tabel_f2_field1; ?>" class="modal fade ubah">
    <div class="modal-dialog">
      <div class="modal-content">
        <?= modal_header(lang('change_data') . ' ' . lang('tabel_f2_alias'), $tl_f2->$tabel_f2_field1) ?>

        <!-- administrator tidak dapat mengubah password akun lain -->
        <form action="<?= site_url($language . '/' . $tabel_f2 . '/update') ?>" method="post"
          enctype="multipart/form-data">
          <div class="modal-body">
            <?= input_hidden('tabel_f2_field1', $tl_f2->$tabel_f2_field1, 'required') ?>
            <?= input_edit('text', 'tabel_f2_field2', $tl_f2->$tabel_f2_field2, 'required readonly') ?>
            <?= edit_min_max('date', 'tabel_f2_field3', $tl_f2->$tabel_f2_field3, 'required readonly', date("Y-m-d"), '') ?>
            <?= edit_min_max('time', 'tabel_f2_field4', $tl_f2->$tabel_f2_field4, 'required readonly', '', '') ?>

            <?= edit_min_max('time', 'tabel_f2_field5', $tl_f2->$tabel_f2_field5, 'required', '', '') ?>
          </div>

          <!-- memunculkan notifikasi modal -->
          <p class="small text-center text-danger"><?= get_flashdata('pesan_ubah') ?></p>

          <div class="modal-footer">
            <?= btn_update() ?>
          </div>
        </form>
      </div>
    </div>
  </div>

  <div id="lihat<?= $tl_f2->$tabel_f2_field1; ?>" class="modal fade lihat" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <?= modal_header(lang('tabel_f2_alias'), $tl_f2->$tabel_f2_field1) ?>

        <!-- administrator tidak bisa melihat password user lain -->
        <form>
          <div class="modal-body">
            <?= table_data(
              row_data('tabel_f2_field2', $tl_f2->$tabel_f2_field2) .
              row_data('tabel_f2_field3', $tl_f2->$tabel_f2_field3) .
              row_data('tabel_f2_field4', $tl_f2->$tabel_f2_field4) .
              row_data('tabel_f2_field5', $tl_f2->$tabel_f2_field5),
              'table-light'
            ) ?>
          </div>

          <!-- memunculkan notifikasi modal -->
          <p class="small text-center text-danger"><?= get_flashdata('pesan_lihat') ?></p>

          <div class="modal-footer">
            <?= btn_tutup() ?>
          </div>
        </form>

      </div>
    </div>
  </div>


  <?= checkbox_js($tl_f2->$tabel_f2_field1) ?>
<?php endforeach ?>

<?= adjust_col_js() ?>