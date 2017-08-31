import jwt from 'jwt-decode'
import cookie from '~/utils/cookie'
import * as Cookies from 'js-cookie'

export const state = () => ({
  locale: 'TH',
  locales: ['TH']
})

export const getters = {
  locale: (state) => {
    return state.locale
  },
  locales: (state) => {
    return state.locales
  }
}

export const mutations = {
  locale: (state, payload) => {
    state.locale = payload.locale
  }
}

export const actions = {
  nuxtServerInit: (context, vc) => {
    if (vc.isServer && vc.req.headers.cookie) {
      var result = cookie(vc)
      try {
        context.commit('auth/authen', { status: true, error: '', name: jwt(result.AT).name, authority: jwt(result.AT).authorities[0] })
        context.commit('locale', { locale: result.locale || 'TH' })
      } catch (e) {
        console.log(e.name + ' : store/index.js')
      }
    } else {
      context.commit('auth/authen', { status: false, error: '' })
    }
  },
  changeLanguage: (context, locale) => {
    Cookies.set('locale', locale)
    context.commit('locale', { locale: locale })
  }

}
