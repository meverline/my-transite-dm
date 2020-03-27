
#include <iostream>
using namespace std;

int SimpleInterest(int P,int T,int R)
{
    float SI;
    
    SI= P * (1+ R*T);//write expression here
    
    return SI;
}

int main() {
	
	cout << SimpleInterest(1000, 2, 13) << endl;
	return 0;
}
