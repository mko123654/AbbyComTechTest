<template>
  <a-card :bordered="false">
    <div id="toolbar">
      <a-button type="primary" icon="plus" @click="$refs.createExamModal.create()">新增</a-button>&nbsp;
      <a-button type="primary" icon="reload" @click="loadAll()">重整</a-button>
    </div>
    <BootstrapTable
      ref="table"
      :columns="columns"
      :data="tableData"
      :options="options"
    />
    <!-- ref是為了方便用this.$refs.modal直接引用，下同 -->
    <step-by-step-exam-modal ref="createExamModal" @ok="handleOk" />
    <exam-edit-modal ref="editExamModal" @ok="handleOk" />
    <update-avatar-modal ref="updateAvatarModal" @ok="handleOk" />
  </a-card>
</template>

<script>
import '../../plugins/bootstrap-table'
import { getExamAll } from '../../api/exam'
import StepByStepExamModal from './modules/StepByStepExamModal'
import ExamEditModal from './modules/ExamEditModal'
import UpdateAvatarModal from '@views/list/modules/UpdateAvatarModal'

export default {
  name: 'ExamTableList',
  components: {
    UpdateAvatarModal,
    ExamEditModal,
    StepByStepExamModal
  },
  data () {
    const that = this // 方便在bootstrap-table中引用methods
    return {
      // Header
      columns: [
        {
          title: '序號',
          field: 'serial',
          formatter: function (value, row, index) {
            return index + 1
          }
        },
        {
          title: '封面',
          field: 'avatar',
          width: 50,
          formatter: (value, row) => {
            return '<div class="exam-avatar">' + value + '</div>'
          },
          events: {
            'click .exam-avatar': function (e, value, row, index) {
              that.handleAvatarEdit(row)
            }
          }
        },
        {
          title: '名稱',
          field: 'name',
          width: 250
        },
        {
          title: '總分數',
          field: 'score'
        },
        {
          title: '建立人員',
          field: 'creator'
        },
        {
          title: '歷時',
          field: 'elapse'
        },
        {
          title: '更新時間',
          field: 'updateTime'
        },
        {
          title: '操作',
          field: 'action',
          width: '150px',
          formatter: (value, row) => {
            return '<button type="button" class="btn btn-success view-exam">詳情</button>' +
              '&nbsp;&nbsp;' +
              '<button type="button" class="btn btn-success edit-exam">編輯</button>'
          },
          events: {
            'click .view-exam': function (e, value, row, index) {
              that.handleSub(row)
            },
            'click .edit-exam': function (e, value, row, index) {
              that.handleEdit(row)
            }
          }
        }
      ],
      tableData: [], // bootstrap-table data
      // custom bootstrap-table
      options: {
        search: true,
        showColumns: true,
        showExport: true,
        pagination: true,
        toolbar: '#toolbar',
        advancedSearch: true,
        idTable: 'advancedTable'
      }
    }
  },
  mounted () {
    this.loadAll()
  },
  methods: {
    handleEdit (record) {
      console.log('開始編輯')
      console.log(record)
      this.$refs.editExamModal.edit(record)
    },
    handleAvatarEdit (record) {
      console.log('開始更新封面')
      console.log(record)
      this.$refs.updateAvatarModal.edit(record)
    },
    handleSub (record) {
      // 直接跳到参加考試的頁面，查看所有題目目的詳情
      const routeUrl = this.$router.resolve({
        path: `/exam/${record.id}`
      })
      window.open(routeUrl.href, '_blank')
    },
    handleOk () {
      this.loadAll()
    },
    loadAll () {
      const that = this
      getExamAll()
        .then(res => {
          if (res.code === 0) {
            that.tableData = res.data
            that.$refs.table._initTable()
          } else {
            that.$notification.error({
              message: '取得考試列表失敗',
              description: res.msg
            })
          }
        })
    }
  }
}
</script>
