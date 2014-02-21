x = 1 : size(hw4rewrite,2);
hw4rewrite2=hw4rewrite(1:2,:)
smooth(hw4rewrite2);

figure()
plot(hw4rewrite2');
axis([0 17 0 12])
xlabel('steps');
ylabel('time (s)')
grid on;
figure()
semilogy(hw4rewrite2');
xlabel('steps');
ylabel('time (s)')
axis([0 17 0 12])
grid on;