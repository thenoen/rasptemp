import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    stateHttpResponse: '',
  },
  mutations: {
    SET_HTTP_DATA : (state, payload) => {
      state.stateHttpResponse = payload;
    },
    CLEAR_HTTP_DATA : (state) => {
      state.stateHttpResponse = "";
    },
  },
  actions: {

  },
});
