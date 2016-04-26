#define BUFFER_LENGTH 64
#define SERIAL_SPEED 9600 // HC-06 works on 9600 bps when you get it from manufacturer
#define DELAY 2000 // in 2 ms
#define cycle 1
#define DISPLAY_INTERVAL  300  


char buffer[BUFFER_LENGTH];
const char at[] = "AT";
const char at_version[] = "AT+VERSION";

//VALUES FOR MODIFICATION
const char at_baud[] = "AT+BAUD34"; // Change the end-number by your self: AT+BAUD#, if # == 4 then 9600, if # ==8 9600
const char at_pin[] = "AT+PIN2016"; // Change the end-number by your self: AT+PIN#
const char at_name[] = "AT+NAMEnanguoyu"; // Change the end-string by your self: AT+NAME#
int mver = 2; int rver = 3; int bver = 4; int lver = 5;//
int mver_led = 6; int rver_led = 7; int bver_led = 8; int lver_led = 9;
int direct; float Degree=1.00;int direct_led;
String squence = "";
bool squencedone= false;
//direct nourth 2 east 3 sourth 4 west 5 

void setup(){
  Serial.begin(9600);
  int errcode;
  delay(DELAY);
  Serial.println("Start scanning: ");
  ask_command(at); // minus one, because strings contain \0 symbol at their end, but HC-06 have to be programmed without this symbol 
  ask_command(at_version);
  ask_command(at_baud);
  ask_command(at_name);
  ask_command(at_pin);
 
  pinMode(mver, OUTPUT);
  pinMode(rver, OUTPUT);
  pinMode(bver, OUTPUT);
  pinMode(lver, OUTPUT);
  pinMode(mver_led, OUTPUT);
  pinMode(rver_led, OUTPUT);
  pinMode(bver_led, OUTPUT);
  pinMode(lver_led, OUTPUT);
  squence.reserve(200);
}
void loop() {
  serialEvent();
  if (squencedone)
  {
    direct = squence[0]-48;
    direct_led = direct+4;
    Degree = 10*(squence[3]-48)+(squence[4]-48);
    Serial.print("DERGREE IS ");
    Serial.println(Degree);
  }
  ver_led(direct_led,Degree);
  squence = "";
  squencedone = false;
}
void serialEvent(){
  while (Serial.available() > 0){
    char insquence = (char)Serial.read();
    squence += insquence;
    if(insquence == '\n'){
      squencedone = true;     
    }
    Serial.println(squence); 
  }
}
void ver_led(int direct_led, float Degree)
{
  Serial.print("The direction is working named:");
  Serial.println(direct_led);
  Serial.print("The degree of route is showned :");
  Serial.println(Degree);
  for (int i = 0; i<cycle; i++) {
    digitalWrite(direct_led, HIGH);
    digitalWrite(direct, HIGH);
    delay(Degree*10);
    digitalWrite(direct_led, LOW);
    digitalWrite(direct, LOW);
    delay(Degree*10);
    Serial.println("led show a time");
    delay(Degree*10);
    }
}
void ask_command(const char *command)
{
  Serial.print("We ask: \t");
  delay(DELAY);
  Serial.print(command);
  delay(DELAY);
  Serial.print('\n');
  if (Serial.available()){
    Serial.print("Device says:\t");
    Serial.readBytes(buffer, BUFFER_LENGTH);
    Serial.print(buffer);
    Serial.print('\n');
  }
  else{
    Serial.println("No answers...");
  }
}
