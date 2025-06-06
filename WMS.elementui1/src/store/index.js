import { Post } from "@/api/http.js";
import {
  getCurrentWarehouseId,
  getToken,
  removeCurrentWarehouseId,
  removeToken,
  setCurrentWarehouseId,
  setToken,
} from "@/utils/cache.js";
import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

const getDefaultState = () => {
  return {
    Token: getToken(),
    UserInfo: null,
    RoleType: undefined,
    HasUserInfo: false,
    CurrentWarehouseId: getCurrentWarehouseId(),
    ColumnType: {
      PHONE: "1",
      DATE: "2",
      DATETIME: "3",
      USERDEFINED: "4",
      DATESET: "5",
      JSON: "6",
      LONGTEXT: "7",
      SHORTTEXT: "8",
      SPLIT: "9",
      POPOVER: "10",
      HIDDEN: "11",
      IMAGES: "12",
      VIDEO: "13",
      CUSTOM: "14",
      AUDIO: "15",
      LINK: "16",
      IMAGE: "17",
      RICHTEXT: "18",
      FILESLINK: "19",
      BARCODE: "20",
      JUDGMENTTAG: "21",
      ToFixed3: "22",
    },
  };
};

const store = new Vuex.Store({
  //  modules: {

  state: getDefaultState(),
  mutations: {
    SET_TOKEN: (state, Token) => {
      state.Token = Token;
    },
    SET_USER_INFO: (state, UserInfo) => {
      state.UserInfo = UserInfo;
      state.HasUserInfo = UserInfo != null;
    },
    SET_ROLE_TYPE: (state, RoleType) => {
      state.RoleType = RoleType;
    },
    SET_CURRENT_WAREHOUSE_ID: (state, CurrentWarehouseId) => {
      state.CurrentWarehouseId = CurrentWarehouseId;
    },
  },
  actions: {
    //登录获取用户的token
    async Login({ commit }, UserInfo) {
      let res = await Post("/User/SignIn", UserInfo);

      if (res.Success) {
        commit("SET_TOKEN", res.Data);
        setToken(res.Data);
      }
      return res;
    },
    //设置当前仓库ID
    async SetCurrentWarehouseId({ commit }, CurrentWarehouseId) {
      commit("SET_CURRENT_WAREHOUSE_ID", CurrentWarehouseId);
      setCurrentWarehouseId(CurrentWarehouseId);
    },
    // 根据token获取用户的信息
    async GetInfo({ commit, state }) {
      let res = await Post("/User/GetByToken", {});

      commit("SET_USER_INFO", res.Data);
      commit("SET_ROLE_TYPE", res.Data.RoleTypeFormat);
    },

    //退出登录
    async Logout({ commit }, UserInfo) {
      removeToken();
      commit("SET_TOKEN", null);
      commit("SET_USER_INFO", null);
      commit("SET_ROLE_TYPE", null);
      commit("SET_CURRENT_WAREHOUSE_ID", null);
      removeCurrentWarehouseId();
    },
  },

  getters: {
    Token: (state) => state.Token,
    UserInfo: (state) => state.UserInfo,
    RoleType: (state) => state.RoleType,
    HasUserInfo: (state) => state.HasUserInfo,
    ColumnType: (state) => state.ColumnType,
    CurrentWarehouseId: (state) => state.CurrentWarehouseId,
    UserId: (state) => state.UserInfo && state.UserInfo.Id,
  },
});

export default store;
