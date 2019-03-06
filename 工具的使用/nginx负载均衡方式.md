# Nginx负载均衡的几种方法

利用nginx的反向代理做负载均衡是我们最常用用法，nginx的负载均衡的方法有**依次循环平均分配(`round-robin`)**、**最少连接优先(`least-connected`)分配**、**利用客户端的ip进行hash计算(`ip-hash`)分配**、**权重(`weighted `)排序分配**四种方法可供我们配置。

## round-robin load balancing

这是nginx默认的负载均衡方式，配置如下：
```json
http {
    upstream myapp1 { # 负载均衡配置
        server srv1.example.com;
        server srv2.example.com;
        server srv3.example.com;
    }

    server { # 代理服务配置
        listen 80; # 代理服务的端口号

        location / { # 所有路径都代理
            proxy_pass http://myapp1;
        }
    }
}
```

当请求进来后，nginx把请求一次循环分配给srv1 - srv3，这种方式简单、平均的优势，但是很多实际情况可能我们每台服务机器的性能可能不一致，这样就会导致性能差的机器过载，性能好的机器空闲。

##  Least connected load balancing

最少连接数优先分配，nginx会去判断服务列表中，当前连接数最少的服务优先分配，配置如下：

```json
upstream myapp1 {
        least_conn;
        server srv1.example.com;
        server srv2.example.com;
        server srv3.example.com;
    }
```

以上两种方式都会导致同一个问题就是分布式环境下session一致性问题。其实两种方式都差不多。

##  IP-Hash load balancing

该方法是根据客户端的ip进行hash函数计算出一个值，然后去分配相关服务（分配的方法可以是hash值%服务数量），该方法有个好处就是不存在session一致性问题，因为客户端IP计算出的hash值都一样，每次分配的服务也是相同的。配置如下：

```json
upstream myapp1 {
    ip_hash;
    server srv1.example.com;
    server srv2.example.com;
    server srv3.example.com;
}
```

##  Weighted load balancing

该方法是根据我们给每个服务分配的权重比例分配相应的服务，配置如下：

```json
  upstream myapp1 {
        server srv1.example.com weight=3;
        server srv2.example.com;
        server srv3.example.com;
    }
```

以上例子，五个请求进来，三个请求会分配在srv1服务上，srv2和srv3服务各分配一个

- 参考文献nginx官方文档【[http://nginx.org/en/docs/http/load_balancing.html]()】