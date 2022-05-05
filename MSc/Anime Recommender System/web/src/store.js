import Vue from 'vue'
import Vuex from 'vuex'
import VueCookies from 'vue-cookies'


Vue.use(Vuex)
Vue.use(VueCookies)

Vue.$cookies.config('7d')

export default new Vuex.Store({
    state: {
        username: '',
        category: []
    },
    mutations: {
        updateName(state, {name}) {
            state.username = name
        },
        addCat(state, {newCat}) {
            if(Array.isArray(newCat)) {
                
                const tmpArry = [...state.category || [], ...newCat]               
                
                // distince value
                state.category = tmpArry.filter((value, index, self) => {
                    return self.indexOf(value) === index
                }) 
                
            } else {
                if(state.category.indexOf(newCat) === -1) {
                    state.category.push(newCat)
                }
            }
        },
        removeCat(state, {cat}) {
            const index = state.category.indexOf(cat)
            state.category.splice(index, 1)
        },
        resetName(state) {
            state.username = ''
        },
        resetCategory(state) {
            state.category = []
        }
    },
    actions: {
        setName(ctx, {name}) {            
            Vue.$cookies.set('username', name)
            ctx.commit('updateName', {name})
        },
        setCategory(ctx, {newCat}) {     
            ctx.commit('addCat', {newCat})
            Vue.$cookies.set('category', this.getters.category)
        },
        reset(ctx) {        
            // cookies has bug cannot be removed    
            Vue.$cookies.set('username', '')
            Vue.$cookies.set('category', [])
            ctx.commit('resetName')
            ctx.commit('resetCategory')
            
        },
        init(ctx) {
            const name = Vue.$cookies.get('username')
            const category = Vue.$cookies.get('category')

            ctx.commit('updateName', {name})            
            if (category) {                                          
                ctx.commit('addCat', {newCat: category.split(',')})   
            }
        }
    },
    getters: {
        username: state => {
            if (state.username) {
                return state.username.length > 0 ? state.username : null
            }
            return null
        },
        category: state => {
            if (state.category) {
                return state.category.length > 0 ? state.category : null
            }
            return null
        },
        user: state => {
            
            if (state.username == null || state.category == null) return null            
            
            return {
                username: state.username,
                category: state.category
            }
        }
    }

    
})
