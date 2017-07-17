import jwt from 'jwt-decode'
import cookie from '~/utils/cookie'

export const actions = {
  nuxtServerInit: (context, vc) => {
    if (vc.isServer && vc.req.headers.cookie) {
      var result = cookie(vc)
      try {
        context.commit('auth/authen', { status: true, error: '', name: jwt(result.AT).name, authority: jwt(result.AT).authorities[0] })
      } catch (e) {
        console.log(e.name + ' : store/index.js')
      }
    } else {
      context.commit('auth/authen', { status: false, error: '' })
    }
  }
}
