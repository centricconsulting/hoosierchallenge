package service

import play.api.{Logger, Application}
import securesocial.core._
import securesocial.core.providers.Token
import securesocial.core.Identity

class CCDUserService(application: Application) extends UserServicePlugin(application){
	private var users = Map[String, Identity]()
	private var tokens = Map[String, Token]()
	
	def find(id: UserIdFromProvider):Option[Identity] = {
	  users.get(id.authId + id.providerId)
	}
	
	def findByEmailAndProvider(email:String, providerId: String):Option[Identity] = {
	  users.values.find(u => u.email.map(e => e == email && u.userIdFromProvider.providerId == providerId).getOrElse(false))
	}
	
	def save(user: Identity) = {
		users = users + (user.userIdFromProvider.authId + user.userIdFromProvider.providerId -> user)
		user
	}
	
	def save(token: Token) {
	  tokens += (token.uuid -> token)
	}
	
	def findToken(token:String): Option[Token] = {
	  tokens.get(token)
	}
	
	def deleteToken(uuid:String) {
	  tokens -= uuid
	}
	
	def deleteTokens() {
	  tokens = Map()
	}
	
	def deleteExpiredTokens() {
	  tokens = tokens.filter(!_._2.isExpired)
	}
}