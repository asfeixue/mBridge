webpackJsonp([1],{100:function(e,t){},104:function(e,t,n){var a=n(8)(n(76),n(108),null,null);e.exports=a.exports},105:function(e,t,n){n(100);var a=n(8)(n(77),n(110),null,null);e.exports=a.exports},106:function(e,t,n){n(98);var a=n(8)(null,n(107),null,null);e.exports=a.exports},107:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-upload",{staticClass:"upload-demo",attrs:{drag:"",action:"/upload/protocols",multiple:""}},[n("i",{staticClass:"el-icon-upload"}),e._v(" "),n("div",{staticClass:"el-upload__text"},[e._v("将文件拖到此处，或"),n("em",[e._v("点击上传")])]),e._v(" "),n("div",{staticClass:"el-upload__tip",slot:"tip"},[e._v("只能上传jpg/png文件，且不超过500kb")])])},staticRenderFns:[]}},108:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement;return(e._self._c||t)("div")},staticRenderFns:[]}},109:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"app"}},[n("div",{},[n("el-menu",{staticClass:"el-menu-demo",attrs:{theme:"dark","default-active":e.activeIndex,mode:"horizontal"},on:{select:e.handleSelect}},[n("el-menu-item",{attrs:{index:"1"}},[e._v("协议管理")]),e._v(" "),n("el-submenu",{attrs:{index:"2"}},[n("template",{slot:"title"},[e._v("导入")]),e._v(" "),n("el-menu-item",{attrs:{index:"2-1"}},[n("router-link",{attrs:{to:"/upload"}},[e._v("上传")])],1),e._v(" "),n("el-menu-item",{attrs:{index:"2-2"}},[e._v("选项2")]),e._v(" "),n("el-menu-item",{attrs:{index:"2-3"}},[e._v("选项3")])],2),e._v(" "),n("el-menu-item",{attrs:{index:"3"}},[n("router-link",{attrs:{to:"/systems"}},[e._v("系统管理")])],1),e._v(" "),n("el-menu-item",{attrs:{index:"4"}},[n("a",{attrs:{target:"_blank"}},[e._v("模板管理")])])],1),e._v(" "),n("div",{staticClass:"line"})],1),e._v(" "),n("div",{},[n("router-view")],1)])},staticRenderFns:[]}},110:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"sysmgmDiv"},[n("el-button",{staticClass:"addBtn",attrs:{type:"primary"},on:{click:e.onAdd}},[e._v("新增")]),e._v(" "),n("div",{staticClass:"editDiv el-col el-col-12 el-col-xs-12 el-col-sm-12",staticStyle:{display:"none"}},[n("el-form",{ref:"form",attrs:{model:e.editData,"label-width":"80px"}},[n("el-form-item",{attrs:{label:"系统名称"}},[n("el-input",{model:{value:e.editData.systemName,callback:function(t){e.editData.systemName=t},expression:"editData.systemName"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"系统编码"}},[n("el-input",{model:{value:e.editData.systemCode,callback:function(t){e.editData.systemCode=t},expression:"editData.systemCode"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"责任人"}},[n("el-input",{model:{value:e.editData.principalUser,callback:function(t){e.editData.principalUser=t},expression:"editData.principalUser"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"系统环境"}},e._l(e.editData.envList,function(t,a){return n("div",{staticStyle:{"text-align":"left"}},[n("el-input",{staticStyle:{width:"30%"},attrs:{value:t.envName},on:{change:function(n){e.addNewEnv(a,t)}},model:{value:t.envName,callback:function(e){t.envName=e},expression:"env.envName"}}),e._v("\n             ---\n            "),n("el-input",{staticStyle:{width:"40%"},attrs:{value:t.envAddress},on:{change:function(n){e.addNewEnv(a,t)}},model:{value:t.envAddress,callback:function(e){t.envAddress=e},expression:"env.envAddress"}}),e._v(" "),n("el-button",{staticClass:"delEditBtn",attrs:{type:"primary",icon:"delete"},on:{click:function(n){e.deleteEnv(t)}}})],1)})),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary"},on:{click:e.onSubmit}},["edit"==e.action?n("span",[e._v("修改")]):e._e(),e._v(" "),"save"==e.action?n("span",[e._v("保存")]):e._e()]),e._v(" "),n("el-button",{on:{click:e.onCancel}},[e._v("取消")])],1)],1)],1),e._v(" "),n("el-table",{staticClass:"sysmgmTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,props:"editData",border:""}},[n("el-table-column",{attrs:{label:"系统code",width:"180"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{staticStyle:{"margin-left":"10px"}},[e._v(e._s(t.row.systemCode))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"系统名称",width:"180"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{staticStyle:{"margin-left":"10px"}},[e._v(e._s(t.row.systemName))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"负责人",width:"180"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{staticStyle:{"margin-left":"10px"}},[e._v(e._s(t.row.principalUser))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"环境列表",width:"180"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("ul",e._l(t.row.envList,function(t){return n("li",[e._v("\n            "+e._s(t.envName)+":"+e._s(t.envAddress)+"\n          ")])}))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"更新时间",width:"180"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{staticStyle:{padding:"0"}},[e._v(e._s(t.row.gmtCreate))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{size:"small"},on:{click:function(n){e.handleEdit(t.$index,t.row)}}},[e._v("编辑")]),e._v(" "),n("el-button",{attrs:{size:"small",type:"danger"},on:{click:function(n){e.handleDelete(t.$index,t.row)}}},[e._v("删除")])]}}])})],1)],1)},staticRenderFns:[]}},31:function(e,t,n){"use strict";var a=n(3),s=n(111),l=n(106),o=n.n(l),i=n(105),d=n.n(i);a.default.use(s.a),t.a=new s.a({routes:[{path:"/upload",name:"Upload",component:o.a},{path:"/systems",name:"Systems",component:d.a}]})},33:function(e,t){},34:function(e,t,n){n(99);var a=n(8)(n(75),n(109),null,null);e.exports=a.exports},75:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"app",methods:{handleOpen:function(e,t){console.log(e,t)},handleClose:function(e,t){console.log(e,t)},handleSelect:function(e,t){console.log(e,t)}},data:function(){return{activeIndex:"1",activeIndex2:"1"}}}},76:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={data:function(){return{form:{name:"",region:"",date1:"",date2:"",delivery:!1,type:[],resource:"",desc:""}}},methods:{onSubmit:function(){console.log("submit!")}}}},77:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n(56),s=n.n(a),l=n(104);n.n(l);t.default={data:function(){return{tableData:[],errors:[],editData:{},action:"edit"}},created:function(){var e=this,t=location.protocol+"//"+location.hostname+":8080";s.a.get(t+"/system/page?systemCode=&page=0&length=10").then(function(t){e.tableData=t.data.body.data}).catch(function(t){e.errors.push(t)})},methods:{handleEdit:function(e,t){document.getElementsByClassName("addBtn")[0].style.display="none",this.editData=t,0==this.editData.envList.length&&this.editData.envList.push({id:0,systemCode:"",envName:"",envAddress:"",gmtCreate:"",gmtModify:""}),document.getElementsByClassName("editDiv")[0].style.display="",document.getElementsByClassName("sysmgmTable")[0].style.display="none"},handleDelete:function(e,t){var n=location.protocol+"//"+location.hostname+":8080";s.a.delete(n+"/system/delete?systemCode="+t.systemCode).then(function(t){t.data.body&&(console.log("delete successfully"),this.editData.envList.splice(e,1))}.bind(this)).catch(function(e){console.log(e)})},onSubmit:function(){var e=location.protocol+"//"+location.hostname+":8080";s.a.post(e+"/system/save",this.editData).then(function(e){console.log("saved successfully")}).catch(function(e){console.log(e)})},onSave:function(){var e=location.protocol+"//"+location.hostname+":8080";s.a.post(e+"/system/save",this.editData).then(function(e){console.log("saved successfullsy")}).catch(function(e){console.log(e)})},onCancel:function(){document.getElementsByClassName("editDiv")[0].style.display="none",document.getElementsByClassName("sysmgmTable")[0].style.display="";var e=document.getElementsByClassName("addBtn")[0];e&&(e.style.display="")},onAdd:function(){this.editData={envList:[{id:0,systemCode:"",envName:"",envAddress:"",gmtCreate:"",gmtModify:""}],gmtCreate:"",gmtModify:"",id:0,principalUser:"",systemCode:"",systemName:""},this.action="save",document.getElementsByClassName("editDiv")[0].style.display="",document.getElementsByClassName("sysmgmTable")[0].style.display="none",document.getElementsByClassName("addBtn")[0].style.display="none"},addNewEnv:function(e,t){this.editData.envList.length-1==e&&this.editData.envList.push({id:0,systemCode:"",envName:"",envAddress:"",gmtCreate:"",gmtModify:""})},deleteEnv:function(e){var t=this.editData.envList.indexOf(e);1!=this.editData.envList.length&&this.editData.envList.splice(t,1)}}}},78:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n(3),s=n(34),l=n.n(s),o=n(31),i=n(32),d=n.n(i),c=n(33);n.n(c);a.default.use(d.a),a.default.config.productionTip=!1,new a.default({el:"#app",router:o.a,template:"<App/>",components:{App:l.a}})},98:function(e,t){},99:function(e,t){}},[78]);
//# sourceMappingURL=app.a4b647ecee51c768c5db.js.map