<template>
  <a-card :bordered="false">
    <div id="toolbar">
      <a-button type="primary" icon="plus" @click="$refs.createQuestionModal.create()">新增</a-button>&nbsp;
      <a-button type="primary" icon="reload" @click="loadAll()">重整</a-button>
    </div>
    <BootstrapTable
      ref="table"
      :columns="columns"
      :data="tableData"
      :options="options"
    />
    <step-by-step-question-modal ref="createQuestionModal" @ok="handleOk" />
    <summernote-update-modal ref="questionUpdateModal" @ok="handleOk" />
    <question-view-modal ref="modalView" @ok="handleOk" />
    <question-edit-modal ref="modalEdit" @ok="handleOk" />
  </a-card>
</template>

<script>
import '../../plugins/bootstrap-table'
import QuestionViewModal from './modules/QuestionViewModal'
import QuestionEditModal from './modules/QuestionEditModal'
import StepByStepQuestionModal from './modules/StepByStepQuestionModal'
import { getQuestionAll, questionUpdate, getQuestionSelection } from '../../api/exam'
import SummernoteUpdateModal from '@views/list/modules/SummernoteUpdateModal'
import $ from 'jquery'

export default {
  name: 'QuestionTableList',
  components: {
    SummernoteUpdateModal,
    StepByStepQuestionModal,
    QuestionViewModal,
    QuestionEditModal
  },
  data () {
    const that = this
    return {
      columns: [
        {
          title: '序號',
          field: 'serial',
          formatter: function (value, row, index) {
            return index + 1
          }
        },
        {
          title: '題目',
          field: 'name',
          width: 200,
          formatter: (value, row) => {
            return '<div class="question-name" style="height: 100%;width: 100%">' + value + '</div>'
          },
          events: {
            'click .question-name': function (e, value, row, index) {
              that.$refs.questionUpdateModal.edit('summernote-question-name-update', row, 'name', '更新題目', questionUpdate)
            }
          }
        },
        {
          title: '解析',
          field: 'description',
          width: 200,
          formatter: (value, row) => {
            return '<div class="question-desc">' + value + '</div>'
          },
          events: {
            'click .question-desc': function (e, value, row, index) {
              that.$refs.questionUpdateModal.edit('summernote-question-desc-update', row, 'description', '更新題目解析', questionUpdate)
            }
          }
        },
        {
          title: '分數',
          field: 'score',
          formatter: (value, row) => {
            return '<div class="question-score">' + value + '</div>'
          },
          events: {
            'click .question-score': function (e, value, row, index) {
              const $element = $(e.target) // 把元素轉換成html
              $element.html('<input type="text" value="' + value + '">')
            }
          }
        },
        {
          title: '建立人員',
          field: 'creator'
        },
        {
          title: '難度',
          field: 'level',
          formatter: (value, row) => {
            return '<div class="question-level">' + value + '</div>'
          },
          events: {
            'click .question-level': function (e, value, row, index) {
              const $element = $(e.target)
              if ($element.children().length > 0) return // 防止重複渲染
              getQuestionSelection().then(res => {
                console.log(res)
                if (res.code === 0) {
                  console.log(res.data)
                  const levels = res.data.levels
                  let inner = '<select>'
                  for (let i = 0; i < levels.length; i++) {
                    if (levels[i].description === value) {
                      // 設定預設的值為目前的值
                      inner += '<option value ="' + levels[i].id + '" name="' + levels[i].name + '" selected="selected">' + levels[i].description + '</option>'
                    } else {
                      inner += '<option value ="' + levels[i].id + '" name="' + levels[i].name + '">' + levels[i].description + '</option>'
                    }
                  }
                  inner += '</select>'
                  $element.html(inner)
                } else {
                  that.$notification.error({
                    message: '取得考題下拉選單失敗',
                    description: res.msg
                  })
                }
              })
            }
          }
        },
        {
          title: '題型',
          field: 'type',
          formatter: (value, row) => {
            return '<div class="question-type">' + value + '</div>'
          },
          events: {
            'click .question-type': function (e, value, row, index) {
              const $element = $(e.target)
              if ($element.children().length > 0) return
              getQuestionSelection().then(res => {
                console.log(res)
                if (res.code === 0) {
                  console.log(res.data)
                  const types = res.data.types
                  let inner = '<select>'
                  for (let i = 0; i < types.length; i++) {
                    if (types[i].description === value) {
                      inner += '<option value ="' + types[i].id + '" name="' + types[i].name + '" selected="selected">' + types[i].description + '</option>'
                    } else {
                      inner += '<option value ="' + types[i].id + '" name="' + types[i].name + '">' + types[i].description + '</option>'
                    }
                  }
                  inner += '</select>'
                  $element.html(inner)
                } else {
                  that.$notification.error({
                    message: '取得考題下拉選單失敗',
                    description: res.msg
                  })
                }
              })
            }
          }
        },
        {
          title: '科目',
          field: 'category',
          formatter: (value, row) => {
            return '<div class="question-category">' + value + '</div>'
          },
          events: {
            'click .question-category': function (e, value, row, index) {
              const $element = $(e.target)
              if ($element.children().length > 0) return
              getQuestionSelection().then(res => {
                console.log(res)
                if (res.code === 0) {
                  console.log(res.data)
                  const categories = res.data.categories
                  let inner = '<select>'
                  for (let i = 0; i < categories.length; i++) {
                    if (categories[i].name === value) {
                      inner += '<option value ="' + categories[i].id + '" name="' + categories[i].description + '" selected="selected">' + categories[i].name + '</option>'
                    } else {
                      inner += '<option value ="' + categories[i].id + '" name="' + categories[i].description + '">' + categories[i].name + '</option>'
                    }
                  }
                  inner += '</select>'
                  $element.html(inner)
                } else {
                  that.$notification.error({
                    message: '取得問題下拉選單失敗',
                    description: res.msg
                  })
                }
              })
            }
          }
        },
        {
          title: '更新時間',
          field: 'updateTime'
        },
        {
          title: '操作',
          field: 'action',
          align: 'center',
          formatter: (value, row) => {
            return '<button type="button" class="btn btn-success view-question">詳情</button>' +
              '&nbsp;&nbsp;' +
              '<button type="button" class="btn btn-success edit-question">編輯</button>'
          },
          events: {
            'click .view-question': function (e, value, row, index) {
              that.handleSub(row)
            },
            'click .edit-question': function (e, value, row, index) {
              that.handleEdit(row)
            }
          }
        }
      ],
      tableData: [],
      // custom bootstrap-table
      options: {
        search: true,
        showColumns: true,
        showExport: true,
        pagination: true,
        toolbar: '#toolbar',
        advancedSearch: true,
        idTable: 'advancedTable',
        // http://www.itxst.com/bootstrap-table-events/tutorial.html
        // onClickRow: that.clickRow,
        // onClickCell: that.clickCell
        onDblClickCell: that.dblClickCell
      }
    }
  },
  mounted () {
    this.loadAll()
  },
  methods: {
    handleEdit (record) {
      this.$refs.modalEdit.edit(record)
    },
    handleSub (record) {
      console.log(record)
      this.$refs.modalView.edit(record)
    },
    handleOk () {
      this.loadAll()
    },
    dblClickCell (field, value, row, $element) {
      if (field === 'score') {
        const childrenInput = $element.children('.question-score').children('input')
        if (childrenInput.length === 0) return
        row.score = childrenInput[0].value
        const that = this
        questionUpdate(row).then(res => {
          console.log(res)
          if (res.code === 0) {
            $element.children('.question-score').text(row.score)
            that.$notification.success({
              message: '更新成功',
              description: '更新成功'
            })
          }
        })
      }

      if (field === 'level') {
        const childrenSelect = $element.children('.question-level').children('select')
        if (childrenSelect.length === 0) return
        const optionSelected = $(childrenSelect[0]).find('option:selected')
        row.levelId = optionSelected.val()
        console.log(row.levelId)
        row.level = optionSelected.text()
        console.log(row.level)
        const that = this
        questionUpdate(row).then(res => {
          console.log(res)
          if (res.code === 0) {
            $element.children('.question-level').text(row.level)
            that.$notification.success({
              message: '更新成功',
              description: '更新成功'
            })
          }
        })
      }

      if (field === 'type') {
        const childrenSelect = $element.children('.question-type').children('select')
        if (childrenSelect.length === 0) return
        const optionSelected = $(childrenSelect[0]).find('option:selected')
        row.typeId = optionSelected.val()
        row.type = optionSelected.text()
        const that = this
        questionUpdate(row).then(res => {
          console.log(res)
          if (res.code === 0) {
            $element.children('.question-type').text(row.type)
            that.$notification.success({
              message: '更新成功',
              description: '更新成功'
            })
          }
        })
      }

      if (field === 'category') {
        const childrenSelect = $element.children('.question-category').children('select')
        console.log(childrenSelect)
        if (childrenSelect.length === 0) return
        const optionSelected = $(childrenSelect[0]).find('option:selected')
        row.categoryId = optionSelected.val()
        row.category = optionSelected.text()
        const that = this
        questionUpdate(row).then(res => {
          console.log(res)
          if (res.code === 0) {
            $element.children('.question-category').text(row.category)
            that.$notification.success({
              message: '更新成功',
              description: '更新成功'
            })
          }
        })
      }
    },
    loadAll () {
      const that = this
      getQuestionAll()
        .then(res => {
          if (res.code === 0) {
            that.tableData = res.data
            that.$refs.table._initTable()
          } else {
            that.$notification.error({
              message: '取得全部考題列表失敗',
              description: res.msg
            })
          }
        })
    }
  }
}
</script>
