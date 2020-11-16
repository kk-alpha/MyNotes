[TOC]

## 树

### [100. 相同的树](https://leetcode-cn.com/problems/same-tree/)

+ ```java
  class Solution {
      public boolean isSameTree(TreeNode p, TreeNode q) {
          if(p!=null&&q!=null&&p.val==q.val){
              return isSameTree(p.left,q.left)&&isSameTree(p.right,q.right);
          }
          else if(p==null&&q==null) return true;
          else return false;
      }
  }
  ```

+ 

### [101. 对称二叉树](https://leetcode-cn.com/problems/symmetric-tree/)

+ ```java
  class Solution {
      public boolean isSymmetric(TreeNode root) {
          return root == null ? true : helper(root.left, root.right);
      } 
      public boolean helper(TreeNode left, TreeNode right){
          if(left!=null && right!=null && left.val == right.val)
              return helper(left.left, right.right) && helper(left.right, right.left);
          else if(left == null && right == null) return true;
          else return false;
      }
  }
  ```

### [104. 二叉树的最大深度](https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/)

+ ```java
  class Solution {
      int max = 0;
      public int maxDepth(TreeNode root) {
          if(root != null){
              return Math.max(maxDepth(root.left),maxDepth(root.right))+1;
          }
          return max;
      }
  }
  ```

+ 

