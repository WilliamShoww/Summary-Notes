1. **CAS原理及缺陷**

   原理：compare and swap 拿预期值去跟最新实际值比较，相同就更新交换值
   
   缺陷：ABA 问题： 一个变量v= a   A线程 a 修改为B   然后B线程a 更新失败  
   
   ​			不能保证原子性
   
   ​			多值需要更新，不好做
   
2. **拆装箱**

3. **String、Builder和Buffer之间的区别**

   可变不可变：String 不可变的，每次都创建一个对象

   线程安全与不安全：Builder线程不安全、String和Buffer线程安全

