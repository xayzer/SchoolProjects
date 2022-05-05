export default function login({to, next, store}) { 

    // nothing set, goes to login
    if (store.getters.user == null) {    
        if(to.name !== 'login') return next({name:'login'})
    }
    // check if username set (no category)    
    if(store.getters.username && !store.getters.category) { 
        
        if(to.name !== 'category') {
            return next({
                name: 'category'
            })
        }
    }
    
    // user is set
    // going any page is okay
    next()    
}