#		getFieldDecorator 的用法

初次接触前端，最终选择目前前端比较流行的技术，`react`框架，而`ant design UI`框架非常全面，所以也使用`ant design` 官方`API`也非常全面，在使用`form`的时候，简单的总结一下`getFieldDecorator `的用法。

##  最简单的用法

介绍及简单用法见[官方文档](<https://ant.design/components/form-cn/>)在偏底部，需要注意的是：

1. 一定要和`Form.create API`配合使用；

2. 包裹的组件不能有`defaultValue`值，否则会报警告并且默认值无效。应该使用`initialValue` 设置初始值；

3. 该方法是在绑定在`props`的`form`属性下的，所以别如下获取：

   ```javascript
   const {getFieldDecorator} = this.props;
   ```

   而是应该如下获取：

   ```javascript
   const {getFieldDecorator} = this.props.form;
   ```

   