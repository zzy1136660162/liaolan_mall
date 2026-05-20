<template>
  <div>
    <el-row :gutter="24">
      <el-col :span="24">
        <el-col v-bind="grid">
          <el-form-item label="排序：">
            <el-input-number
              controls-position="right"
              v-model="formValidate.sort"
              :min="0"
              placeholder="请输入排序"
              :disabled="isDisabled"
            />
          </el-form-item>
        </el-col>
        <el-col v-bind="grid">
          <el-form-item label="积分：">
            <el-input-number
              controls-position="right"
              v-model="formValidate.giveIntegral"
              :min="0"
              placeholder="请输入排序"
              :disabled="isDisabled"
            />
          </el-form-item>
        </el-col>
        <el-col v-bind="grid">
          <el-form-item label="虚拟销量：">
            <el-input-number
              controls-position="right"
              v-model="formValidate.ficti"
              :min="0"
              placeholder="请输入排序"
              :disabled="isDisabled"
            />
          </el-form-item>
        </el-col>
      </el-col>
      <el-col :span="24">
        <el-form-item label="商品推荐：">
          <el-checkbox-group v-model="checkboxGroup" size="small" @change="$emit('changeGroup')" :disabled="isDisabled">
            <el-checkbox v-for="(item, index) in recommend" :key="index" :label="item.value">{{
              item.name
            }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="活动优先级：">
          <div class="color-list acea-row row-middle">
            <div
              :disabled="isDisabled"
              class="color-item"
              :class="activityMap[item]"
              v-for="item in formValidate.activity"
              :key="item"
              draggable="true"
              @dragstart="$emit('dragStart', $event, item)"
              @dragover.prevent="$emit('dragOver', $event, item)"
              @dragenter="$emit('dragEnter', $event, item)"
              @dragend="$emit('dragEnd', $event, item)"
            >
              {{ item }}
            </div>
            <div class="tip">可拖动按钮调整活动的优先展示顺序</div>
          </div>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="优惠券：" class="proCoupon">
          <div>
            <el-tag
              v-for="(tag, index) in formValidate.coupons"
              :key="index"
              class="mr10"
              :closable="!isDisabled"
              :disable-transitions="false"
              @close="$emit('closeCoupon', tag)"
            >
              {{ tag.name }}
            </el-tag>
            <span class="mr10" v-if="formValidate.couponIds == null">暂无优惠券</span>
            <el-button v-if="!isDisabled" class="mr15" @click="$emit('addCoupon')">选择优惠券</el-button>
          </div>
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'ProductOtherSettings',
  props: {
    formValidate: {
      type: Object,
      required: true,
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
    checkboxGroup: {
      type: Array,
      default: () => [],
    },
    recommend: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      grid: {
        xl: 7,
        lg: 7,
        md: 7,
        sm: 24,
        xs: 24,
      },
      activityMap: {
        默认: 'red',
        秒杀: 'blue',
        砍价: 'green',
        拼团: 'yellow',
      },
    };
  },
};
</script>

<style scoped lang="scss">
.color-item {
  height: 30px;
  line-height: 30px;
  padding: 0 10px;
  color: #fff;
  margin-right: 10px;
}

.color-list .color-item.blue {
  background-color: #1e9fff;
}

.color-list .color-item.yellow {
  background-color: rgb(254, 185, 0);
}

.color-list .color-item.green {
  background-color: #009688;
}

.color-list .color-item.red {
  background-color: #ed4014;
}

.proCoupon {
  ::v-deep el-form-item__content {
    margin-top: 5px;
  }
}
</style>
