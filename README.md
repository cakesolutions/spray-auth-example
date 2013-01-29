
## Authentication example using Spray

### Documentation
The documentation is scattered throughout the code, as comments.

### How to test it
This is an sbt project. This means that, if you have SBT installed, you
can easily start this project with "sbt run".
After the server is running, point your browser to these routes:

* ```http://0.0.0.0:8080/resource/all```
* ```http://0.0.0.0:8080/resource/customer-only?auth_token=customer```
* ```http://0.0.0.0:8080/resource/admin-only?auth_token=admin```
* ```http://0.0.0.0:8080/resource/better-customer?auth_token=customer```
* ```http://0.0.0.0:8080/resource/better-admin?auth_token=admin```

The last two are there just to show how to improve the code. (see ```authentication.scala```.)
Try also to call the same routes but omitting the get parameter.
Bear in mind that this is just to give you a gist, but Spray authentication
mechanism is so flexible and powerful that it's up to you to decide how and
what enable/disable. Bear in mind that to enable/disable some route according
to certain roles, you don't need two different authentication handlers at all:
you could just wrap all your logic inside one handler, but here we decided to
use separate handlers just to better explain the workflow.

### Caching
By default, we're not aware of any caching mechanism embedded in Spray, because
Spray is just a tiny, high performant layer above your app which gives you
a flexible routing scheme and other goodies like tools for contacting external
services etc, etc, so the caching it's something you can to provide. The better
place to implement caching is probably in the ```AuthController```, calling
a caching system (for example, you can use H2 to implement an in-memory
caching, or Redis, or MongoDB, whatever).
