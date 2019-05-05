<template>
  <div class="hello">
    <h1>{{ msg }}</h1>

    <button v-on:click="getHttpData()">make call</button>
    <button v-on:click="clearHttpData()">clear</button>
    <div>
      {{ httpResponse }}
    
	  <b-alert show variant="secondary">Secondary Alert - bootstrap-vue</b-alert>
   
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import axios from 'axios';


@Component({
  data() {
    return {
      dataHttpResponse: '',
    };
  },
})
export default class HelloWorld extends Vue {
  @Prop()
  private msg!: string;
  // @Prop()
  private httpResponse: string = '';

  private getHttpData() {
    axios.get(`/latestValue`)
    .then((response) => {
      // JSON responses are automatically parsed.
      this.httpResponse = response.data;
      this.$store.commit('SET_HTTP_DATA',response.data);
    })
    .catch((error) => this.httpResponse = error.response.data);
  }

  private clearHttpData() {
      this.$store.commit('CLEAR_HTTP_DATA');
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
