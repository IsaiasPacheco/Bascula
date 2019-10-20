#include <16f877a.h> 
#device adc=10
#fuses XT, NOWDT              
#use delay (clock=4000000)  
#use rs232 ( baud=9600, parity=N, xmit=pin_c6, rcv=pin_c7, bits=8 )


void main(){
   
   int16 VoltoSensor1;   
   float Sensor1;
   
   SET_TRIS_B(0x00); //Configura el puerto B como salida
   output_b(0x00);

   
   setup_adc_ports(AN0);
   //tiempo de adquisicion del convertidor, si no se quiere usar se coloca ADC_OFF
   //se puede usar ADC_CLOCK_DIV_2|4|8|16|32|64
   setup_adc(ADC_CLOCK_DIV_2);
   

   
   
   while( 1 ) {
   
   Sensor1 = 0;
   
   //Se especifica que canal vamos a utilizar
   set_adc_channel(0); 
   //Se recomienda colocar este tiempo
   delay_ms(50);
   
   VoltoSensor1=read_adc();
   
   Sensor1=(VoltoSensor1*5.0)/1024.0;
   
   
   //Se imprime el valor del sensor 1
   printf( "%f \r\n", Sensor1);  
   

   
   }

}


