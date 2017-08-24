export const state = () => {
  return {
    category: {}
  }
}

export const getters = {
  category: (state) => {
    return state.category
  }
}

export const mutations = {
  setcategory: (state, payload) => {
    state.category = payload.category
  }
}

export const actions = {
  setcategory: (context, vc) => {
    context.commit('setcategory', { category: vc })
  }
}
