<template>
    <div>
        <el-container style="height: 100vh">

            <el-header>
                <div class="bg-header">
                    <div class="flex align-center">
                        <img :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/logo.png'" style="width: 50px; height: 50px;">
                        <span style="margin-left:10px">智慧仓库管理系统</span>
                    </div>
                    <div style="display: flex;align-items: center">
                        <div>当前仓库:</div>

                        <!-- element选择器组件根据id和name显示选择仓库 -->
                    
                        <el-select v-model="CurrentWarehouseId" placeholder="请选择" @change="ChangeWarehouse"
                            style="margin-left: 10px;margin-right: 10px;">
                            <el-option v-for="item in warehouseList" :key="item.Id" :label="item.Name" :value="item.Id">
                            </el-option>
                        </el-select>


                        <el-avatar :size="60" :src="UserInfo.ImageUrls" size="medium">
                            <img :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.png'" />
                        </el-avatar>
                        <div style="margin-left:10px">
                            <el-dropdown>
                                <span class="el-dropdown-link" style="color:white">
                                    {{ UserInfo.Name }}<i class="el-icon-arrow-down el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item>
                                        <div @click="ToPath('/Admin/UserPerson')">个人信息</div>

                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                        <div @click="ToPath('/Admin/PasswordEdit')">修改密码</div>

                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                        <div @click="LoginOut()">退出</div>
                                    </el-dropdown-item>

                                </el-dropdown-menu>
                            </el-dropdown>
                        </div>

                    </div>
                </div>


            </el-header>


            <el-container>
                <el-aside width="200px">

                    <el-menu class="menu-list" :router="true" active-text-color="var(--primary-color)">
                        <el-menu-item index="/Admin/Home">
                            <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%8A%A5%E8%A1%A8%20%281%29.png'"></img>
                            <span>控制台</span>
                        </el-menu-item>
                        <template v-if="RoleType == '管理员'">
                            <el-submenu index="/Admin/UserList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E7%94%A8%E6%88%B7%20%281%29.png'"></img>
                                    <span>用户管理</span>
                                </template>
                                <el-menu-item index="/Admin/UserList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E7%94%A8%E6%88%B7.png'"></img>
                                    <span>用户信息</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/WarehouseRelativeUserList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%85%B3%E8%81%94.png'"></img>
                                    <span>仓库用户</span>
                                </el-menu-item>
                            </el-submenu>
                            <el-submenu index="/Admin/WarehouseList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%9F%BA%E7%A1%80%E8%B5%84%E6%96%99-.png'"></img>
                                    <span>基础资料</span>
                                </template>
                                <el-menu-item index="/Admin/WarehouseList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E4%BB%93%E5%BA%93.png'"></img>
                                    <span>仓库</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/WarehouseBinList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%BA%93%E4%BD%8D%E6%95%B0.png'"></img>
                                    <span>库位</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/CustomerList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%90%88%E4%BD%9C%E8%B4%A7%E4%B8%BB.png'"></img>
                                    <span>货主</span>
                                </el-menu-item>

                            </el-submenu>
                            <el-submenu index="/Admin/CargoTypeList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%95%86%E5%93%81.png'"></img>
                                    <span>商品管理</span>
                                </template>
                                <el-menu-item index="/Admin/CargoTypeList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%95%86%E5%93%81%E5%88%86%E7%B1%BB.png'"></img>
                                    <span>商品分类</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/CargoList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%95%86%E5%93%81.png'"></img>
                                    <span>商品</span>
                                </el-menu-item>


                            </el-submenu>
                            <el-submenu index="/Admin/StockInList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%85%A5%E5%BA%93%E7%AE%A1%E7%90%86.png'"></img>
                                    <span>入库管理</span>
                                </template>
                                <el-menu-item index="/Admin/StockInList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%85%A5%E5%BA%93%E5%8D%95.png'"></img>
                                    <span>入库单</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/StockInEchart">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%8A%A5%E8%A1%A8.png'"></img>
                                    <span>入库统计</span>
                                </el-menu-item>

                            </el-submenu>
                            <el-submenu index="/Admin/StockOutList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%87%BA%E5%BA%93%E7%AE%A1%E7%90%86.png'"></img>
                                    <span>出库管理</span>
                                </template>
                                <el-menu-item index="/Admin/StockOutList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%96%B0%E5%A2%9E%E5%87%BA%E5%BA%93%E5%8D%95.png'"></img>
                                    <span>出库单</span>
                                </el-menu-item>

                                <el-menu-item index="/Admin/StockOutEchart">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%8A%A5%E8%A1%A8.png'"></img>
                                    <span>出库统计</span>
                                </el-menu-item>

                            </el-submenu>


                            <el-submenu index="/Admin/InventoryCheckList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%BA%93%E5%86%85%E7%AE%A1%E7%90%86.png'"></img>
                                    <span>库内管理</span>
                                </template>
                                <el-menu-item index="/Admin/InventoryCheckList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E7%9B%98%E7%82%B9%E5%8D%95.png'"></img>
                                    <span>盘点单</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/InventoryRecordList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E4%B8%BB%E6%9C%BA%E8%AE%B0%E5%BD%95.png'"></img>
                                    <span>库存记录</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/InventoryRuningRecordList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E4%B8%BB%E6%9C%BA%E8%AE%B0%E5%BD%95.png'"></img>
                                    <span>库存流水</span>
                                </el-menu-item>
                            </el-submenu>
                        </template>
                        <template v-if="RoleType == '员工'">
                            <el-submenu index="/Admin/WarehouseList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%9F%BA%E7%A1%80%E8%B5%84%E6%96%99-.png'"></img>
                                    <span>基础资料</span>
                                </template>

                                <el-menu-item index="/Admin/WarehouseBinList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%BA%93%E4%BD%8D%E6%95%B0.png'"></img>
                                    <span>库位</span>
                                </el-menu-item>


                            </el-submenu>
                            <el-submenu index="/Admin/CargoTypeList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%95%86%E5%93%81.png'"></img>
                                    <span>商品管理</span>
                                </template>
                                <el-menu-item index="/Admin/CargoTypeList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%95%86%E5%93%81%E5%88%86%E7%B1%BB.png'"></img>
                                    <span>商品分类</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/CargoList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%95%86%E5%93%81.png'"></img>
                                    <span>商品</span>
                                </el-menu-item>


                            </el-submenu>
                            <el-submenu index="/Admin/StockInList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%85%A5%E5%BA%93%E7%AE%A1%E7%90%86.png'"></img>
                                    <span>入库管理</span>
                                </template>
                                <el-menu-item index="/Admin/StockInList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%85%A5%E5%BA%93%E5%8D%95.png'"></img>
                                    <span>入库单</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/StockInEchart">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%8A%A5%E8%A1%A8.png'"></img>
                                    <span>入库统计</span>
                                </el-menu-item>
                            </el-submenu>
                            <el-submenu index="/Admin/StockOutList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%87%BA%E5%BA%93%E7%AE%A1%E7%90%86.png'"></img>
                                    <span>出库管理</span>
                                </template>
                                <el-menu-item index="/Admin/StockOutList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%96%B0%E5%A2%9E%E5%87%BA%E5%BA%93%E5%8D%95.png'"></img>
                                    <span>出库单</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/StockOutEchart">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E6%8A%A5%E8%A1%A8.png'"></img>
                                    <span>出库统计</span>
                                </el-menu-item>


                            </el-submenu>


                            <el-submenu index="/Admin/InventoryCheckList">
                                <template slot="title">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E5%BA%93%E5%86%85%E7%AE%A1%E7%90%86.png'"></img>
                                    <span>库内管理</span>
                                </template>
                                <el-menu-item index="/Admin/InventoryCheckList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E7%9B%98%E7%82%B9%E5%8D%95.png'"></img>
                                    <span>盘点单</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/InventoryRecordList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E4%B8%BB%E6%9C%BA%E8%AE%B0%E5%BD%95.png'"></img>
                                    <span>库存记录</span>
                                </el-menu-item>
                                <el-menu-item index="/Admin/InventoryRuningRecordList">
                                    <img class="meun-ico" :src="'https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/%E4%B8%BB%E6%9C%BA%E8%AE%B0%E5%BD%95.png'"></img>
                                    <span>库存流水</span>
                                </el-menu-item>
                            </el-submenu>
                        </template>





                    </el-menu>
                </el-aside>

                <el-main class="admin-main">
                    <el-breadcrumb separator-class="el-icon-arrow-right" class="margin-bottom-xs">
                        <el-breadcrumb-item v-for="(item, index) in breadList" :key="index" :to="item.path">{{
                            item.meta.title
                        }}</el-breadcrumb-item>
                    </el-breadcrumb>

                    <transition>
                        <router-view></router-view>
                    </transition>

                </el-main>

            </el-container>
        </el-container>
    </div>
</template>

<script>

import { adminRouters } from "@/router/index";
import { mapGetters } from "vuex";
export default {
    name: 'Layout',
    data() {
        return {
            routerMenu: [],
            breadList: [],
            warehouseList: [],
            "CurrentWarehouseId": null,

        }
    },
    computed: {
        ...mapGetters([
            'Token', 'UserInfo', 'RoleType', 'RoleTypeFormat', 'HasUserInfo', 'ColumnType', "UserId",
        ])
    },
    watch: {
        $route() {
            this.getBreadcrumb();
        },
    },

    created() {
        this.CurrentWarehouseId = this.$store.getters.CurrentWarehouseId;

        this.routerMenu = adminRouters;
        this.getBreadcrumb();
        this.GetWarehouseList();
    },
    methods: {
        //跳转
        async ToPath(url) {
            this.$router.push({
                path: url
            })
        },

        
        // 获取仓库列表，通过id拿到用户的仓库信息，在element官网复制select选择器的代码
        async GetWarehouseList() {
            let { Data: { Items } } = await this.$Post("/WarehouseRelativeUser/List", { UserId: this.UserId })
            let list = Items.map(item => item.WarehouseDto);
            for (let item of list) {
                item.Id = item.Id.toString();
            }
            this.warehouseList = list;
            if (!this.CurrentWarehouseId) {

                this.CurrentWarehouseId = this.warehouseList[0].Id;
                this.$store.dispatch("SetCurrentWarehouseId", this.warehouseList[0].Id);
            }
        },


        async ChangeWarehouse(id) {

            this.$store.dispatch("SetCurrentWarehouseId", id);
            window.location.reload();
        },
        async LoginOut() {

            await this.$store.dispatch('Logout')
            window.location.reload('/Login')
        },
        isHome(route) {
            return route.path === "/Admin";
        },
        getBreadcrumb() {
            let matched = this.$route.matched;
            if (!this.isHome(matched[0])) {
                matched = [{ path: "/Admin", meta: { title: "控制台" } }].concat(matched);
            }
            this.breadList = matched;

        }
    }
}
</script>

<style>
.el-header,
.el-footer {

    text-align: center;
    line-height: 60px;
    padding: 0px !important;
}

.el-aside {

    color: #333;
    text-align: center;
    line-height: 200px;
}


.menu-list {
    height: calc(100vh - 60px);
}

.el-submenu__title {
    text-align: left
}

.el-menu-item {
    text-align: left;
}

.admin-main {
    height: calc(100vh - 60px) !important;
}

.meun-ico {
    width: 25px;
    height: 25px;
    margin-right: 5px;
}
</style>