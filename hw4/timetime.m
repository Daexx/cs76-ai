x = 1 : 6;
hw4rewrite2 = [4.550875 
    1.7096875
0.8725625
0.062
0.015472222
0.623277778
]

semilogy(x, hw4rewrite2, '-s');
xlabel('steps');
ylabel('time (s)')
axis([1 6 0 12])
grid on;