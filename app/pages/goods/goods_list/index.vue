<template>
	<view :data-theme="theme" class="page-wrap">
		<view class='productList'>
			<view class="header-bar">
				<view class='search-box'>
					<text class="search-icon">⌕</text>
					<input placeholder='搜索商品名称' placeholder-class='search-placeholder'
						confirm-type='search' v-model='where.keyword' @confirm="doSearch"
						maxlength="20" />
					<view class="search-clear" v-if="where.keyword" @click="clearSearch">
						<text class="clear-icon">✕</text>
					</view>
				</view>
				<view class='view-switch' @click='Changswitch'>
					<text class="switch-icon" v-if="is_switch">☷</text>
					<text class="switch-icon" v-else>☰</text>
				</view>
				<view class='search-btn' @click='doSearch'>搜索</view>
			</view>

			<view class="sort-bar">
				<view class='sort-tab' @click='set_where(2)'>
					<text>价格</text>
					<view class="arrows">
						<view class="arrow arrow-up" :class="{active: price==1}"></view>
						<view class="arrow arrow-down" :class="{active: price==2}"></view>
					</view>
				</view>
				<view class='sort-tab' @click='set_where(3)'>
					<text>销量</text>
					<view class="arrows">
						<view class="arrow arrow-up" :class="{active: stock==1}"></view>
						<view class="arrow arrow-down" :class="{active: stock==2}"></view>
					</view>
				</view>
				<view class='sort-tab' :class='{on: nows}' @click='set_where(4)'>
					<text>新品</text>
				</view>
				<view class='sort-tab filter-trigger' @click='showFilter = true'>
					<text>筛选</text>
					<view class="filter-dot" v-if="hasActiveFilter"></view>
				</view>
			</view>
			<view class="category-bar" v-if="title">
				<text class="category-name">{{title}}</text>
			</view>
			<view class="category-bar category-bar-placeholder" v-else></view>

			<view class="filter-panel" :class="{show: showFilter}">
				<view class="filter-header">
					<text class="filter-title">行业筛选</text>
					<view class="filter-close" @click="showFilter = false">✕</view>
				</view>
				<view class="filter-section">
					<view class="filter-label">电压等级</view>
					<view class="filter-tags">
						<view class="filter-tag"
							:class="{active: activeFilter.voltageLevel === item}"
							v-for="(item, idx) in voltageFilterOptions"
							:key="idx"
							@click="handleVoltageFilter(item)">
							{{item}}
						</view>
					</view>
				</view>
				<view class="filter-section">
					<view class="filter-label">导体材质</view>
					<view class="filter-tags">
						<view class="filter-tag"
							:class="{active: activeFilter.conductorMaterial === item}"
							v-for="(item, idx) in materialFilterOptions"
							:key="idx"
							@click="handleMaterialFilter(item)">
							{{item}}
						</view>
					</view>
				</view>
				<view class="filter-section">
					<view class="filter-label">阻燃等级</view>
					<view class="filter-tags">
						<view class="filter-tag"
							:class="{active: activeFilter.flameRetardantGrade === item}"
							v-for="(item, idx) in flameFilterOptions"
							:key="idx"
							@click="handleFlameFilter(item)">
							{{item}}
						</view>
					</view>
				</view>
				<view class="filter-footer">
					<view class="filter-btn reset" @click="resetFilter">重置</view>
					<view class="filter-btn confirm" @click="confirmFilter">确定</view>
				</view>
			</view>

			<view class="main-content" :style="{paddingTop: showFilter ? '624rpx' : '236rpx'}">
				<view v-if="productList.length > 0" class="card-list"
					:class="[is_switch ? 'grid-mode' : 'list-mode']">
					<view class="card-item"
						v-for="(item, index) in productList" :key="index"
						@click="godDetail(item)">
						<view class="card-media">
							<image class="media-img" :src="item.image" mode="aspectFill" lazy-load></image>
							<view v-if="item.activityStyle" class="activity-border"
								:style="{backgroundImage: 'url(' + item.activityStyle + ')'}">
							</view>
							<view v-if="item.activityH5 && item.activityH5.type" class="activity-tag">
								{{item.activityH5.type === '1' ? '秒杀' : item.activityH5.type === '2' ? '砍价' : '拼团'}}
							</view>
						</view>
						<view class="card-info">
							<view class="info-name">{{item.storeName}}</view>
							<view class="info-tags" v-if="item.voltageLevel || item.conductorMaterial || item.flameRetardantGrade">
								<text class="ind-tag" v-if="item.voltageLevel">{{item.voltageLevel}}</text>
								<text class="ind-tag" v-if="item.conductorMaterial">{{item.conductorMaterial}}</text>
								<text class="ind-tag" v-if="item.flameRetardantGrade">{{item.flameRetardantGrade}}</text>
							</view>
							<view class="info-bottom">
								<view class="price-row">
									<view class="price-wrap">
										<text class="currency">¥</text>
										<text class="amount">{{item.price}}</text>
									</view>
									<text class="ot-price" v-if="item.otPrice && Number(item.otPrice) > Number(item.price)">¥{{item.otPrice}}</text>
								</view>
								<view class="meta-row">
									<text class="vip-label" v-if="item.vip_price > 0">会员价</text>
									<text class="sales-num">已售{{Number(item.sales) || 0}}</text>
								</view>
							</view>
						</view>
					</view>
				</view>

				<view v-else-if="!loading && where.page > 1" class="empty-block">
					<view class="empty-icon">
						<view class="box-frame">
							<view class="bar bar-a"></view>
							<view class="bar bar-b"></view>
							<view class="bar bar-c"></view>
						</view>
					</view>
					<view class="empty-msg">该分类下暂无商品</view>
					<view class="empty-sub">试试其他分类或调整筛选条件</view>
					<view class="empty-btn" @click="goHome">浏览全部商品</view>
				</view>

				<view class="load-more" v-if="productList.length > 0">
					<view v-if="loading" class="loading-dots">
						<view class="dot" v-for="n in 3" :key="n"></view>
					</view>
					<text class="load-text">{{loadTitle}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {getProductslist} from '@/api/store.js';
	import {mapGetters} from "vuex";
	let app = getApp();
	export default {
		computed: mapGetters(['uid']),
		data() {
			return {
				urlDomain: this.$Cache.get("imgHost"),
				productList: [],
				is_switch: true,
				where: {
					keyword: '',
					priceOrder: '',
					salesOrder: '',
					news: 0,
					page: 1,
					limit: 20,
					cid: '',
				},
				price: 0,
				stock: 0,
				nows: false,
				loadend: false,
				loading: false,
				loadTitle: '加载更多',
				title: '',
				theme: app.globalData.theme,
				refreshState: false,
				showFilter: false,
				voltageFilterOptions: ['0.6/1kV', '10kV', '35kV'],
				materialFilterOptions: ['铜', '铝'],
				flameFilterOptions: ['ZR', 'NH', 'WDZ'],
				activeFilter: {
					voltageLevel: '',
					conductorMaterial: '',
					flameRetardantGrade: '',
				},
				hasActiveFilter: false
			};
		},
		onLoad(options) {
			this.$set(this.where, 'cid', options.cid || '');
			this.title = options.title ? decodeURIComponent(options.title) : '';
			this.$set(this.where, 'keyword', options.searchValue || '');
			this.get_product_list();
		},
		onShow() {
		},
		methods: {
			goHome() {
				uni.switchTab({ url: '/pages/index/index' });
			},
			doSearch() {
				const keyword = (this.where.keyword || '').trim();
				if (!keyword) {
					uni.showToast({ title: '请输入搜索关键词', icon: 'none', duration: 1500 });
					return;
				}
				this.title = '';
				this.loadend = false;
				this.$set(this.where, 'page', 1);
				this.get_product_list(true);
			},
			clearSearch() {
				this.$set(this.where, 'keyword', '');
				this.loadend = false;
				this.$set(this.where, 'page', 1);
				this.get_product_list(true);
			},
			godDetail(item) {
				console.log('godDetail called', item);
				if (!item || !item.id) {
					console.log('item or item.id is missing');
					return;
				}
				uni.navigateTo({
					url: '/pages/goods/goods_details/index?id=' + item.id
				});
			},
			Changswitch() {
				this.is_switch = !this.is_switch;
			},
			set_where(e) {
				switch (e) {
					case 1:
						return;
					case 2:
						this.price = this.price == 0 ? 1 : this.price == 1 ? 2 : 0;
						this.stock = 0;
						break;
					case 3:
						this.stock = this.stock == 0 ? 1 : this.stock == 1 ? 2 : 0;
						this.price = 0;
						break;
					case 4:
						this.nows = !this.nows;
						break;
				}
				this.loadend = false;
				this.$set(this.where, 'page', 1);
				this.get_product_list(true);
			},
			setWhere() {
				if (this.price == 0) this.where.priceOrder = '';
				else if (this.price == 1) this.where.priceOrder = 'asc';
				else if (this.price == 2) this.where.priceOrder = 'desc';
				if (this.stock == 0) this.where.salesOrder = '';
				else if (this.stock == 1) this.where.salesOrder = 'asc';
				else if (this.stock == 2) this.where.salesOrder = 'desc';
				this.where.news = this.nows ? 1 : 0;
				this.where.voltageLevel = this.activeFilter.voltageLevel || '';
				this.where.conductorMaterial = this.activeFilter.conductorMaterial || '';
				this.where.flameRetardantGrade = this.activeFilter.flameRetardantGrade || '';
				if (!this.where.voltageLevel) delete this.where.voltageLevel;
				if (!this.where.conductorMaterial) delete this.where.conductorMaterial;
				if (!this.where.flameRetardantGrade) delete this.where.flameRetardantGrade;
			},
			toggleFilter(key, value) {
				if (this.activeFilter[key] === value) {
					this.activeFilter[key] = '';
				} else {
					this.activeFilter[key] = value;
				}
				this.updateHasFilter();
			},
			handleVoltageFilter(value) { this.toggleFilter('voltageLevel', value); },
			handleMaterialFilter(value) { this.toggleFilter('conductorMaterial', value); },
			handleFlameFilter(value) { this.toggleFilter('flameRetardantGrade', value); },
			updateHasFilter() {
				this.hasActiveFilter = !!(this.activeFilter.voltageLevel || this.activeFilter.conductorMaterial || this.activeFilter.flameRetardantGrade);
			},
			resetFilter() {
				this.activeFilter = { voltageLevel: '', conductorMaterial: '', flameRetardantGrade: '' };
				this.hasActiveFilter = false;
			},
			confirmFilter() {
				this.showFilter = false;
				this.loadend = false;
				this.$set(this.where, 'page', 1);
				this.get_product_list(true);
			},
			get_product_list(isPage) {
				const that = this;
				that.setWhere();
				if (that.loadend || that.loading) return;
				if (isPage === true) that.$set(that, 'productList', []);
				that.loading = true;
				that.loadTitle = '';
				getProductslist(that.where).then(res => {
					const list = res.data.list || [];
					const productList = that.$util.SplitArray(list, that.productList);
					const loadend = list.length < that.where.limit;
					that.loadend = loadend;
					that.loading = false;
					that.loadTitle = loadend ? '已全部加载' : '加载更多';
					that.$set(that, 'productList', productList);
					that.$set(that.where, 'page', that.where.page + 1);
					if (that.productList.length === 0 && isPage === true) {
						that.loadTitle = '暂无商品';
					}
				}).catch(() => {
					that.loading = false;
					that.loadTitle = '加载失败，点击重试';
				});
			},
			onRefresh() {
				if (this.refreshState) return;
				this.refreshState = true;
				this.loadend = false;
				this.$set(this.where, 'page', 1);
				getProductslist(this.where).then(res => {
					const list = res.data.list || [];
					this.$set(this, 'productList', list);
					this.loadend = list.length < this.where.limit;
					this.loadTitle = this.loadend ? '已全部加载' : '加载更多';
					this.$set(this.where, 'page', 2);
				}).catch(() => {
					this.loadTitle = '刷新失败';
				}).finally(() => {
					this.refreshState = false;
					uni.stopPullDownRefresh();
				});
			},
			loadMore() {
				if (this.productList.length > 0 && !this.loadend && !this.loading) {
					this.get_product_list();
				}
			}
		},
		onPullDownRefresh() { this.onRefresh(); },
		onReachBottom() { this.loadMore(); }
	}
</script>

<style scoped lang="scss">
$blue:         #003da6;
$blue-light:   #0052d9;
$blue-ghost:   #e8ecff;
$dark:         #0f1118;
$dark2:        #2c2f3a;
$dark3:        #5a5e6e;
$light3:       #9ca0b0;
$line:         #e0e3ef;
$bg:           #f4f5fa;
$white:        #ffffff;
$input-bg:     #eef0fa;
$red:          #d42a1f;
$orange:       #f5a623;
$r-sm:  8rpx;
$r-md:  14rpx;
$r-lg:  20rpx;
$ease:   cubic-bezier(0.22, 1, 0.36, 1);
$ease-b: cubic-bezier(0.34, 1.56, 0.64, 1);

.page-wrap { min-height: 100vh; background: $bg; }
.productList { min-height: 100vh; display: flex; flex-direction: column; }

.header-bar {
	position: fixed; top: 0; left: 0; right: 0; z-index: 99;
	height: 96rpx; padding: 0 28rpx;
	display: flex; align-items: center; gap: 16rpx;
	background: $white;
	box-shadow: 0 1rpx 0 $line;
}
.search-box {
	flex: 1; height: 64rpx; background: $input-bg;
	border-radius: 32rpx; padding: 0 24rpx;
	display: flex; align-items: center; gap: 10rpx;
	transition: box-shadow 250ms $ease;
	&:focus-within { box-shadow: 0 0 0 4rpx rgba($blue, 0.1); }
	.search-icon { font-size: 28rpx; color: $light3; flex-shrink: 0; }
	input { flex: 1; height: 100%; font-size: 26rpx; color: $dark; }
	.search-clear {
		width: 36rpx; height: 36rpx;
		display: flex; align-items: center; justify-content: center;
		border-radius: 50%; background: rgba($light3, 0.3);
		flex-shrink: 0;
		transition: all 200ms $ease;
		&:active { transform: scale(0.85); background: rgba($light3, 0.5); }
		.clear-icon { font-size: 18rpx; color: $white; line-height: 1; }
	}
}
.search-placeholder { color: $light3; font-size: 24rpx; }
.view-switch {
	width: 60rpx; height: 60rpx;
	display: flex; align-items: center; justify-content: center;
	border-radius: $r-sm;
	transition: all 200ms $ease;
	&:active { background: $input-bg; transform: scale(0.9); }
	.switch-icon { font-size: 30rpx; color: $blue; line-height: 1; }
}
.search-btn {
	margin-left: 8rpx; padding: 0 24rpx; height: 60rpx; line-height: 60rpx;
	background: $blue; color: #fff; font-size: 24rpx; font-weight: 600;
	border-radius: $r-md;
	transition: all 200ms $ease;
	&:active { transform: scale(0.95); opacity: 0.9; }
}

.sort-bar {
	position: fixed; top: 96rpx; left: 0; right: 0; z-index: 98;
	height: 76rpx; display: flex; align-items: center;
	background: $white; box-shadow: 0 1rpx 0 $line;
}
.sort-tab {
	flex: 1; height: 100%;
	display: flex; align-items: center; justify-content: center; gap: 6rpx;
	font-size: 24rpx; font-weight: 500; color: $dark3;
	position: relative; transition: color 200ms $ease;
	&:last-child { flex: 0 0 auto; width: 96rpx; }
	&::after {
		content: ''; position: absolute; bottom: 0; left: 50%;
		transform: translateX(-50%) scaleX(0); width: 32rpx; height: 4rpx;
		background: $blue; border-radius: 2rpx;
		transition: transform 300ms $ease;
	}
	&.on { color: $blue; font-weight: 700; &::after { transform: translateX(-50%) scaleX(1); } }
	&:active { opacity: 0.5; }
}
.arrows { display: flex; flex-direction: column; gap: 2rpx; }
.arrow {
	width: 0; height: 0;
	border-left: 6rpx solid transparent; border-right: 6rpx solid transparent;
	opacity: 0.2; transition: opacity 200ms $ease;
}
.arrow-up { border-bottom: 6rpx solid $dark3; &.active { border-bottom-color: $blue; opacity: 1; } }
.arrow-down { border-top: 6rpx solid $dark3; &.active { border-top-color: $blue; opacity: 1; } }
.filter-trigger {
	position: relative;
	.filter-dot {
		position: absolute; top: 16rpx; right: 16rpx;
		width: 8rpx; height: 8rpx; border-radius: 50%; background: $blue;
	}
}

.category-bar {
	position: fixed; top: 172rpx; left: 0; right: 0; z-index: 97;
	min-height: 64rpx; padding: 0 28rpx;
	display: flex; align-items: center; justify-content: center;
	background: $white;
	box-shadow: 0 1rpx 0 $line;
}
.category-bar-placeholder {
	visibility: hidden;
}
.category-name {
	font-size: 24rpx; font-weight: 600; color: $dark;
	max-width: 100%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	padding: 0 16rpx;
	text-align: center;
}

.filter-panel {
	position: fixed; top: 236rpx; left: 0; right: 0; z-index: 97;
	background: $white; padding: 28rpx; display: none;
	box-shadow: 0 12rpx 32rpx rgba(0,0,0,0.08);
	&.show { display: block; animation: panelIn 350ms $ease; }
}
@keyframes panelIn {
	from { opacity: 0; transform: translateY(-16rpx); }
	to { opacity: 1; transform: translateY(0); }
}
.filter-header {
	display: flex; justify-content: space-between; align-items: center; margin-bottom: 20rpx;
	.filter-title { font-size: 28rpx; font-weight: 700; color: $dark; }
	.filter-close { width: 40rpx; height: 40rpx; display: flex; align-items: center; justify-content: center; font-size: 26rpx; color: $light3; &:active { opacity: 0.5; } }
}
.filter-section {
	margin-bottom: 20rpx;
	.filter-label { font-size: 24rpx; font-weight: 600; color: $dark2; margin-bottom: 12rpx; }
	.filter-tags { display: flex; flex-wrap: wrap; gap: 10rpx; }
	.filter-tag {
		padding: 10rpx 22rpx; font-size: 22rpx; color: $dark3;
		background: $input-bg; border: 1rpx solid $line; border-radius: $r-sm;
		transition: all 200ms $ease;
		&.active { color: #fff; background: $blue; border-color: $blue; }
		&:active { transform: scale(0.95); }
	}
}
.filter-footer {
	display: flex; gap: 16rpx; margin-top: 24rpx;
	.filter-btn {
		flex: 1; height: 72rpx; display: flex; align-items: center; justify-content: center;
		font-size: 26rpx; font-weight: 600; border-radius: $r-md;
		transition: all 200ms $ease;
		&:active { transform: scale(0.97); }
		&.reset { color: $dark3; background: $input-bg; border: 1rpx solid $line; }
		&.confirm { color: #fff; background: $blue; box-shadow: 0 4rpx 16rpx rgba($blue, 0.25); }
	}
}

.main-content { padding-bottom: 40rpx; }

.card-list {
	padding: 20rpx 24rpx 32rpx;
	display: grid; grid-template-columns: repeat(2, 1fr); gap: 20rpx;
	&.list-mode { grid-template-columns: 1fr; gap: 16rpx; }
}

.card-item {
	background: $white; border-radius: $r-md; overflow: hidden;
}
@keyframes cardIn {
	from { opacity: 0; transform: translateY(24rpx) scale(0.96); }
	to { opacity: 1; transform: translateY(0) scale(1); }
}

.card-media {
	position: relative; aspect-ratio: 1 / 1;
	background: linear-gradient(145deg, $blue-ghost 0%, #dde1f5 100%);
	overflow: hidden;
}
.media-img {
	width: 100%; height: 100%; object-fit: cover;
}
.activity-border { position: absolute; inset: 0; background-size: cover; background-position: center; pointer-events: none; }
.activity-tag {
	position: absolute; top: 12rpx; left: 12rpx;
	padding: 4rpx 14rpx; font-size: 18rpx; font-weight: 700; color: #fff;
	background: linear-gradient(135deg, $blue 0%, $blue-light 100%);
	border-radius: $r-sm; letter-spacing: 0.06em;
	box-shadow: 0 4rpx 10rpx rgba($blue, 0.3); z-index: 2;
}

.card-info {
	padding: 18rpx 18rpx 20rpx;
	display: flex; flex-direction: column; gap: 8rpx; flex: 1;
}
.info-name {
	font-size: 26rpx; font-weight: 600; color: $dark;
	line-height: 1.48;
	display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
	overflow: hidden; word-break: break-all;
}
.info-tags { display: flex; flex-wrap: wrap; gap: 6rpx; margin-top: 2rpx; }
.ind-tag {
	display: inline-flex; align-items: center; height: 30rpx; padding: 0 8rpx;
	font-size: 16rpx; font-weight: 500; color: $blue;
	background: rgba($blue, 0.05); border: 1rpx solid rgba($blue, 0.1);
	border-radius: 4rpx; line-height: 1;
}
.info-bottom { display: flex; flex-direction: column; gap: 6rpx; margin-top: auto; padding-top: 4rpx; }
.price-row { display: flex; align-items: baseline; gap: 8rpx; }
.price-wrap { display: flex; align-items: baseline; gap: 2rpx; }
.currency { font-size: 20rpx; font-weight: 700; color: $red; line-height: 1; }
.amount { font-size: 34rpx; font-weight: 800; color: $red; line-height: 1; letter-spacing: -0.02em; }
.ot-price { font-size: 18rpx; color: $light3; text-decoration: line-through; line-height: 1; }
.meta-row { display: flex; align-items: center; justify-content: space-between; }
.vip-label {
	font-size: 16rpx; font-weight: 700;
	color: darken($orange, 20%); background: rgba($orange, 0.12);
	padding: 2rpx 8rpx; border-radius: 3rpx;
}
.sales-num { font-size: 18rpx; color: $light3; }

.grid-mode .card-item {
	display: flex; flex-direction: column;
	.card-info { flex: 1; display: flex; flex-direction: column; }
	.info-bottom { margin-top: auto; }
}

.list-mode .card-item {
	display: flex; flex-direction: row;
	.card-media { width: 220rpx; aspect-ratio: 1; flex-shrink: 0; }
	.card-info { flex: 1; justify-content: center; padding: 18rpx 20rpx 18rpx 18rpx; gap: 10rpx; }
	.info-name { font-size: 28rpx; }
	.ind-tag { height: 34rpx; padding: 0 12rpx; font-size: 18rpx; }
	.amount { font-size: 36rpx; }
}

.load-more { display: flex; align-items: center; justify-content: center; padding: 40rpx 0 64rpx; gap: 12rpx; }
.loading-dots { display: flex; gap: 8rpx; }
.dot {
	width: 10rpx; height: 10rpx; border-radius: 50%; background: $blue;
	animation: dotPulse 1s ease-in-out infinite;
	&:nth-child(2) { animation-delay: 0.12s; }
	&:nth-child(3) { animation-delay: 0.24s; }
}
@keyframes dotPulse {
	0%, 80%, 100% { transform: scale(0.5); opacity: 0.2; }
	40% { transform: scale(1); opacity: 1; }
}
.load-text { font-size: 22rpx; color: $light3; }

.empty-block {
	display: flex; flex-direction: column; align-items: center;
	padding: 160rpx 32rpx 80rpx; text-align: center;
	animation: fadeUp 500ms $ease both;
}
@keyframes fadeUp {
	from { opacity: 0; transform: translateY(24rpx); }
	to { opacity: 1; transform: translateY(0); }
}
.empty-icon { margin-bottom: 36rpx; }
.box-frame {
	width: 128rpx; height: 128rpx; border: 2rpx solid $line; border-radius: 14rpx;
	display: flex; flex-direction: column; align-items: center; justify-content: center;
	gap: 10rpx; padding: 24rpx;
	animation: breathe 3s ease-in-out infinite;
}
.bar { width: 100%; height: 3rpx; background: $line; border-radius: 2rpx; &.bar-b { width: 60%; } }
@keyframes breathe { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-8rpx); } }
.empty-msg { font-size: 28rpx; font-weight: 700; color: $dark; margin-bottom: 8rpx; }
.empty-sub { font-size: 22rpx; color: $light3; margin-bottom: 40rpx; }
.empty-btn {
	display: inline-flex; align-items: center; justify-content: center;
	height: 72rpx; padding: 0 48rpx; background: $blue; color: #fff;
	border-radius: $r-md; font-size: 26rpx; font-weight: 700;
	transition: all 200ms $ease;
	&:active { transform: scale(0.95); opacity: 0.9; }
}
.bg_color { @include main_bg_color(theme); }
.font_color { @include main_color(theme); }
</style>
