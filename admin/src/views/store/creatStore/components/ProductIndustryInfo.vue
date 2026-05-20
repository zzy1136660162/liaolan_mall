<template>
  <el-row :gutter="24">
    <el-col :span="24">
      <div class="section-title mb20">
        <span class="section-title-bar"></span>
        <span class="section-title-text">电线电缆行业参数</span>
        <span class="section-title-desc ml10" style="color:#999;font-size:12px;">以下参数为电线电缆产品结构化信息，便于参数筛选和展示</span>
      </div>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="产品型号：">
        <el-input class="from-ipt-width" v-model="formValidate.industryInfo.modelNo" placeholder="如 YJV、KVV、VV" :disabled="isDisabled" />
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="电压等级：">
        <el-select class="from-ipt-width" v-model="formValidate.industryInfo.voltageLevel" placeholder="请选择电压等级" :disabled="isDisabled" clearable filterable allow-create>
          <el-option v-for="item in voltageLevelOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="导体材质：">
        <el-select class="from-ipt-width" v-model="formValidate.industryInfo.conductorMaterial" placeholder="请选择导体材质" :disabled="isDisabled" clearable>
          <el-option label="铜" value="铜" />
          <el-option label="铝" value="铝" />
          <el-option label="铝合金" value="铝合金" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="芯数：">
        <el-select class="from-ipt-width" v-model="formValidate.industryInfo.coreCount" placeholder="请选择芯数" :disabled="isDisabled" clearable filterable allow-create>
          <el-option v-for="item in coreCountOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="截面积：">
        <el-select class="from-ipt-width" v-model="formValidate.industryInfo.crossSectionArea" placeholder="请选择截面积" :disabled="isDisabled" clearable filterable allow-create>
          <el-option v-for="item in crossSectionOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="护套材质：">
        <el-select class="from-ipt-width" v-model="formValidate.industryInfo.sheathMaterial" placeholder="请选择护套材质" :disabled="isDisabled" clearable>
          <el-option label="PVC" value="PVC" />
          <el-option label="PE" value="PE" />
          <el-option label="XLPE" value="XLPE" />
          <el-option label="橡皮" value="橡皮" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="阻燃等级：">
        <el-select class="from-ipt-width" v-model="formValidate.industryInfo.flameRetardantGrade" placeholder="请选择阻燃等级" :disabled="isDisabled" clearable>
          <el-option label="ZR（阻燃）" value="ZR" />
          <el-option label="NH（耐火）" value="NH" />
          <el-option label="WDZ（低烟无卤）" value="WDZ" />
          <el-option label="无" value="无" />
        </el-select>
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="执行标准：">
        <el-input class="from-ipt-width" v-model="formValidate.industryInfo.standardCode" placeholder="如 GB/T 12706.1" :disabled="isDisabled" />
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="适用场景：">
        <el-input class="from-ipt-width" v-model="formValidate.industryInfo.applicationScene" placeholder="如 厂区供电、控制回路" :disabled="isDisabled" />
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'ProductIndustryInfo',
  props: {
    formValidate: {
      type: Object,
      required: true,
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      grid2: {
        xl: 24,
        lg: 24,
        md: 24,
        sm: 24,
        xs: 24,
      },
      voltageLevelOptions: ['300/500V', '450/750V', '0.6/1kV', '1.8/3kV', '3.6/6kV', '6/10kV', '8.7/10kV', '10kV', '35kV'],
      coreCountOptions: ['1芯', '2芯', '3芯', '4芯', '5芯', '3+1芯', '3+2芯', '4+1芯', '5+1芯'],
      crossSectionOptions: ['1.5mm²', '2.5mm²', '4mm²', '6mm²', '10mm²', '16mm²', '25mm²', '35mm²', '50mm²', '70mm²', '95mm²', '120mm²', '150mm²', '185mm²', '240mm²', '300mm²', '400mm²'],
    };
  },
};
</script>

<style scoped lang="scss">
.section-title {
  display: flex;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}
.section-title-bar {
  display: inline-block;
  width: 4px;
  height: 18px;
  background: #003da6;
  border-radius: 2px;
  margin-right: 10px;
}
.section-title-text {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
