import 'package:dart_app/client.g.dart';

main() async {
  final client = MyClient(baseUrl: "http://localhost:3000");
  final result = await client.sayHello(SayHelloParams(name: "John"));
  print(result.message);
}
