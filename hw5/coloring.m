
times = [14.783 1.208 30.869 0.028 14.87 0.029];
n = 1 : length(times);
bar(times);



set(gca,'xticklabel',{'BackTrack','MRV','LCV', 'MRV+LCV','MAC3','ALL'});
a=num2str(times');
text(n-0.25,times+1,a)

xlabel('Methods');
ylabel('time (s)');