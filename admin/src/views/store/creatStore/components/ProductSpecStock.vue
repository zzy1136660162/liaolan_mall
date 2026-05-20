<template>
  <el-row>
    <el-col :span="24">
      <el-form-item label="商品规格：" props="specType">
        <el-radio-group
          v-model="formValidate.specType"
          @change="$emit('changeSpecType', formValidate.specType)"
          :disabled="isDisabled"
        >
          <el-radio :label="false" class="radio">单规格</el-radio>
          <el-radio :label="true">多规格</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="佣金设置：" props="isSub">
        <el-radio-group
          v-model="formValidate.isSub"
          @change="$emit('changeSubType', formValidate.isSub)"
          :disabled="isDisabled"
        >
          <el-radio :label="true" class="radio">单独设置</el-radio>
          <el-radio :label="false">默认设置</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-col>
    <el-col v-if="formValidate.specType && !isDisabled" :span="24" class="noForm">
      <el-form-item label="选择规格：" prop="">
        <div class="acea-row">
          <el-select v-model="formValidate.selectRule" @change="$emit('selectRule', formValidate.selectRule)">
            <el-option v-for="item in ruleList" :key="item.id" :label="item.ruleName" :value="item.id" />
          </el-select>
          <el-button class="ml10" @click="$emit('addRule')">添加规格</el-button>
        </div>
      </el-form-item>
      <el-form-item>
        <div v-for="(item, index) in formValidate.attr" :key="index">
          <div class="acea-row row-middle">
            <span class="mr5">{{ item.attrName }}</span
            ><i class="el-icon-circle-close" @click="$emit('removeAttr', index)" />
          </div>
          <div class="rulesBox">
            <el-tag
              v-for="(j, indexn) in item.attrValue"
              :key="indexn"
              closable
              size="medium"
              :disable-transitions="false"
              class="mb5 mr10"
              @close="$emit('closeAttr', item.attrValue, indexn, index)"
            >
              {{ j }}
            </el-tag>
            <template v-if="!$route.params.isCopy">
              <el-input
                v-if="item.inputVisible"
                ref="saveTagInput"
                v-model="item.attrValue.attrsVal"
                class="input-new-tag"
                size="small"
                @keyup.enter.native="$emit('createAttr', item.attrValue.attrsVal, index)"
                @blur="$emit('createAttr', item.attrValue.attrsVal, index)"
              />
              <el-button v-else class="button-new-tag" size="small" @click="$emit('showInput', item)">+ 添加</el-button>
            </template>
          </div>
        </div>
      </el-form-item>
      <el-col v-if="isBtn">
        <div class="acea-row">
          <el-form-item label="规格：" class="inputWid">
            <el-input v-model="formDynamic.attrsName" placeholder="请输入规格" />
          </el-form-item>
          <el-form-item label="规格值：" class="inputWid">
            <el-input v-model="formDynamic.attrsVal" placeholder="请输入规格值" />
          </el-form-item>
          <el-form-item class="noLeft">
            <el-button type="primary" @click="$emit('createAttrName')">确定</el-button>
            <el-button @click="$emit('offAttrName')">取消</el-button>
          </el-form-item>
        </div>
      </el-col>
      <el-form-item v-if="!isBtn && !$route.params.isCopy">
        <el-button type="primary" icon="md-add" class="mr15" @click="$emit('addBtn')">添加新规格</el-button>
      </el-form-item>
    </el-col>
    <el-col v-if="formValidate.attr.length > 0 && formValidate.specType && !isDisabled" :span="24" class="noForm">
      <el-form-item label="批量设置：">
        <el-table :data="oneFormBatch" border class="tabNumWidth" size="mini">
          <el-table-column label="图片" min-width="80">
            <template slot-scope="scope">
              <div class="upLoadPicBox" @click="$emit('openPicModal', '1', 'pi')">
                <div v-if="scope.row.image" class="pictrue tabPic">
                  <el-image :preview-src-list="isDisabled ? [scope.row.image] : []" :src="scope.row.image" />
                </div>
                <div v-else class="upLoad tabPic">
                  <i class="el-icon-camera cameraIconfont" />
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            v-for="(item, iii) in attrValue"
            :key="iii"
            :label="formThead[iii].title"
            min-width="120"
          >
            <template slot-scope="scope">
              <el-input
                v-model="scope.row[iii]"
                :type="formThead[iii].title === '商品编号' ? 'text' : 'number'"
                :min="0"
                class="priceBox"
                @keyup.native="$emit('keyupEvent', iii, scope.row[iii], scope.$index, 1)"
              />
            </template>
          </el-table-column>
          <template v-if="formValidate.isSub">
            <el-table-column label="一级返佣(元)" min-width="120">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.brokerage"
                  type="number"
                  :min="0"
                  :max="scope.row.price"
                  class="priceBox"
                />
              </template>
            </el-table-column>
            <el-table-column label="二级返佣(元)" min-width="120">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.brokerageTwo"
                  type="number"
                  :min="0"
                  :max="scope.row.price"
                  class="priceBox"
                />
              </template>
            </el-table-column>
          </template>
          <el-table-column label="操作" width="80">
            <template>
              <a class="submission" @click="$emit('batchAdd')">批量添加</a>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-col>
    <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
      <el-form-item v-if="formValidate.specType === false">
        <el-table :data="OneattrValue" border class="tabNumWidth" size="mini">
          <el-table-column label="图片" min-width="80">
            <template slot-scope="scope">
              <div class="upLoadPicBox" @click="$emit('openPicModal', '1', 'dan', 'pi')">
                <div v-if="formValidate.image" class="pictrue tabPic">
                  <el-image :preview-src-list="isDisabled ? [scope.row.image] : []" :src="scope.row.image" />
                </div>
                <div v-else class="upLoad tabPic">
                  <i class="el-icon-camera cameraIconfont" />
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            v-for="(item, iii) in attrValue"
            :key="iii"
            :label="formThead[iii].title"
            min-width="120"
          >
            <template slot-scope="scope">
              <el-input
                :disabled="isDisabled"
                v-model="scope.row[iii]"
                :type="formThead[iii].title === '商品编号' ? 'text' : 'number'"
                :min="0"
                class="priceBox"
                @keyup.native="$emit('keyupEvent', iii, scope.row[iii], scope.$index, 2)"
              />
            </template>
          </el-table-column>
          <template v-if="formValidate.isSub">
            <el-table-column label="一级返佣(元)" min-width="120">
              <template slot-scope="scope">
                <el-input
                  :disabled="isDisabled"
                  v-model="scope.row.brokerage"
                  type="number"
                  :min="0"
                  class="priceBox"
                />
              </template>
            </el-table-column>
            <el-table-column label="二级返佣(元)" min-width="120">
              <template slot-scope="scope">
                <el-input
                  :disabled="isDisabled"
                  v-model="scope.row.brokerageTwo"
                  type="number"
                  :min="0"
                  class="priceBox"
                />
              </template>
            </el-table-column>
          </template>
        </el-table>
      </el-form-item>
      <el-form-item label="全部sku：" v-if="$route.params.id && showAll">
        <el-button type="default" @click="$emit('showAllSku')" :disabled="isDisabled">展示</el-button>
      </el-form-item>
      <el-form-item
        v-if="formValidate.attr.length > 0 && formValidate.specType"
        label="商品属性："
        class="labeltop"
        :class="isDisabled ? 'disLabel' : 'disLabelmoren'"
      >
        <el-table :data="ManyAttrValue" border class="tabNumWidth" size="mini">
          <template v-if="manyTabDate">
            <el-table-column
              v-for="(item, iii) in manyTabDate"
              :key="iii"
              :label="manyTabTit[iii].title"
              min-width="80"
            >
              <template slot-scope="scope">
                <span class="priceBox" v-text="scope.row[iii]" />
              </template>
            </el-table-column>
          </template>
          <el-table-column label="图片" min-width="80">
            <template slot-scope="scope">
              <div class="upLoadPicBox" @click="$emit('openPicModal', '1', 'duo', scope.$index)">
                <div v-if="scope.row.image" class="pictrue tabPic">
                  <el-image
                    class="preview-src"
                    :preview-src-list="isDisabled ? [scope.row.image] : []"
                    :src="scope.row.image"
                  />
                </div>
                <div v-else class="upLoad tabPic">
                  <i class="el-icon-camera cameraIconfont" />
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            v-for="(item, iii) in attrValue"
            :key="iii"
            :label="formThead[iii].title"
            min-width="120"
          >
            <template slot-scope="scope">
              <el-input
                :disabled="isDisabled"
                v-model="scope.row[iii]"
                :type="formThead[iii].title === '商品编号' ? 'text' : 'number'"
                class="priceBox"
                @keyup.native="$emit('keyupEvent', iii, scope.row[iii], scope.$index, 3)"
              />
            </template>
          </el-table-column>
          <el-table-column label="一级返佣(元)" min-width="120" v-if="formValidate.isSub">
            <template slot-scope="scope">
              <el-input
                :disabled="isDisabled"
                v-model="scope.row.brokerage"
                type="number"
                :min="0"
                :max="scope.row.price"
                class="priceBox"
              />
            </template>
          </el-table-column>
          <el-table-column label="二级返佣(元)" min-width="120" v-if="formValidate.isSub">
            <template slot-scope="scope">
              <el-input
                :disabled="isDisabled"
                v-model="scope.row.brokerageTwo"
                type="number"
                :min="0"
                :max="scope.row.price"
                class="priceBox"
              />
            </template>
          </el-table-column>
          <el-table-column v-if="!isDisabled" key="3" label="操作" min-width="80">
            <template slot-scope="scope">
              <a class="submission" @click="$emit('delAttrTable', scope.$index)">删除</a>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'ProductSpecStock',
  props: {
    formValidate: {
      type: Object,
      required: true,
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
    ruleList: {
      type: Array,
      default: () => [],
    },
    OneattrValue: {
      type: Array,
      default: () => [],
    },
    ManyAttrValue: {
      type: Array,
      default: () => [],
    },
    oneFormBatch: {
      type: Array,
      default: () => [],
    },
    formThead: {
      type: Object,
      default: () => ({}),
    },
    attrValue: {
      type: Array,
      default: () => [],
    },
    manyTabDate: {
      type: Object,
      default: () => ({}),
    },
    manyTabTit: {
      type: Object,
      default: () => ({}),
    },
    showAll: {
      type: Boolean,
      default: false,
    },
    isBtn: {
      type: Boolean,
      default: false,
    },
    formDynamic: {
      type: Object,
      default: () => ({}),
    },
  },
};
</script>

<style scoped lang="scss">
.upLoadPicBox {
  ::v-deep.el-alert {
    padding: 0 !important;
  }
}

.disLabel {
  ::v-deepel-form-item__label {
    margin-left: 36px !important;
  }
}

.disLabelmoren {
  ::v-deepel-form-item__label {
    margin-left: 120px !important;
  }
}

.tabPic {
  width: 40px !important;
  height: 40px !important;

  img {
    width: 100%;
    height: 100%;
  }
}

.noLeft {
  ::v-deepel-form-item__content {
    margin-left: 0 !important;
  }
}

.tabNumWidth {
  ::v-deepel-input-number--medium {
    width: 121px !important;
  }

  ::v-deepel-input-number__increase {
    width: 20px !important;
    font-size: 12px !important;
  }

  ::v-deepel-input-number__decrease {
    width: 20px !important;
    font-size: 12px !important;
  }

  ::v-deepel-input-number--medium .el-input__inner {
    padding-left: 25px !important;
    padding-right: 25px !important;
  }

  ::v-deep thead {
    line-height: normal !important;
  }

  ::v-deep .el-table .cell {
    line-height: normal !important;
  }
}

.button-new-tag {
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.pictrue {
  width: 60px;
  height: 60px;
  border: 1px dotted rgba(0, 0, 0, 0.1);
  margin-right: 10px;
  position: relative;
  cursor: pointer;

  img,
  .image {
    width: 100%;
    height: 100%;
  }

  video {
    width: 100%;
    height: 100%;
  }
}

.labeltop {
  ::v-deepel-form-item__label {
    float: none !important;
    display: inline-block !important;
    width: auto !important;
  }
}

.inputWid {
  width: 500px;
}

.noLeft {
  margin-left: -60px;
}
</style>
