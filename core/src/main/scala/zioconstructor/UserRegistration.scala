package zioconstructor

import zio.Task

// service - alternative definition (already w/ impls)
class UserRegistration(notifier: UserNotifier, userModel: UserModel) {
  def register(u: User): Task[User] = {
    for {
      _ <- userModel.insert(u)
      _ <- notifier.notify(u, "Welcome!")
    } yield u
  }
}
