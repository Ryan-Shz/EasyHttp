# EasyHttp
一个基于RxJava和Retrofit封装的网络请求库。链式调用，使用简单，提供丰富的操作符支撑各种功能的实现，灵活且高扩展。

## Usage

### 初始化

EasyHttp使用Retrofit来执行网络请求。在开始请求之前，我们需要先对EasyHttp做一些初始化工作，主要用来指定Retrofit对象的构建选项：

```
OkHttpClient mBaseOkHttpClient = new OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build();
// 构建RetrofitBuilder
RetrofitBuilder builder = new RetrofitBuilder().client(mBaseOkHttpClient);
// 传递给EasyHttp完成初始化
EasyHttp.setRetrofitBuilder(builder);
```

### 扩展EasyHttp类

继承EasyHttp实现你自己的Http类:

```
public class TestHttp<T> extends EasyHttp<T> {

    public static <T> TestHttp<T> from(Class<? extends T> targetClass) {
        return new TestHttp<>(targetClass);
    }

    private TestHttp(Class<? extends T> target) {
        super(target);
    }

}
```

一般APP内会有多个域名，提供多个服务，每个服务的API接入方式可能是不一样的，比如基础参数，数据格式，加密方式等等，EasyHttp提供继承扩展的方式，每个服务可以通过继承EasyHttp来实现自己的Http类。比如PushHttp, UserHttp...等等。

### 发起请求

#### 异步请求

```
TestHttp.from(TestResult.class)
        .setFullUrl(TEST_API_URL)
        .bindLifecycle(this)
        .addParam("key1", "value1")
        .addParam("key2", "value2")
        .get(new HttpCallback<TestResult>() {
            @Override
            public void onSuccess(TestResult result) {
                super.onSuccess(result);
                toast(result.getResponseDetails());
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                toast(e.getMessage());
            }
        });
```

#### 同步请求

```
TestResult result = TestHttp.from(TestResult.class)
        .setFullUrl(TEST_API_URL)
        .addParam("key1", "value1")
        .addParam("key2", "value2")
        .sync()
        .get();
```

#### 异步文件下载

```
File targetFile = new File(getCacheDir(), "test");
TestHttp.from(File.class)
        .targetFile(targetFile)        
        .setFullUrl(TEST_FILE_URL)
        .download(new DownloadCallback<File>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
            }

            @Override
            public void onProgress(int progress) {
                super.onProgress(progress);
            }
        });
```

#### 同步文件下载

```
File targetFile = new File(getCacheDir(), "test");
File file = TestHttp.from(File.class)
        .targetFile(targetFile)
        .setFullUrl(TEST_FILE_URL)
        .sync()
        .download();
```

### 配置选项

EasyHttp提供了丰富的配置选项和操作符以支持更多的功能。

#### Url设置

```
setBaseUrl(String baseUrl) // 设置基础地址
setPath(String path) // 设置路径
setFullUrl(String url) // 设置完整路径
```

它们的关系：fullUrl = baseUrl + path

当你实现自己的Http扩展类时，可以在类的构造方法中添加setBaseUrl(String baseUrl)来设置基础地址，这样外部在使用时，只需要调用setPath(String path)即可。

#### 添加参数

```
addParam(String key, String value) // 添加普通参数
addParams(Map<String, Object> params) // 添加参数集合
addHeaderParam(String key, Object value) // 添加头部参数
```

普通参数形式：

1. 如果是get请求，参数会添加到query params中。
2. 如果是post请求，参数会添加RequestBody表单中。
3. 其他请求，参数会添加RequestBody表单中。

头部参数则会被放入Http请求的Header中。

#### 生命周期绑定

EasyHttp通过rxlifecycle来管理请求的生命周期。提供以下两个配置方法：

```
bindLifecycle(LifecycleProvider provider)
bindLifecycleUtilEvent(LifecycleTransformer transformer)
```

bindLifecycle(LifecycleProvider provider)方法需要传入一个lifecycleProvider对象，它由rxlifecycle库提供。应用程序Activity可以通过继承EasyHttpActivity或EasyHttpFragment来轻松的继承该特性，它们都实现了LifecycleProvider接口。

bindLifecycle方法默认在Activity的onDestroy中解绑请求，如果需要自定义请求的解绑时机，可以通过bindLifecycleUtilEvent(LifecycleTransformer transformer)方法来实现，比如在Activity的onPause中解绑请求：

```
EasyHttp.from(TestResult.class)
				.bindLifecycleUtilEvent(this.bindUntilEvent(ActivityEvent.PAUSE))
```

#### 参数拦截器

参数拦截器用来拦截参数的创建过程。通过参数拦截器，我们可以很方便的给指定的Http扩展类添加公共的头部参数和普通参数。

```
addHeaderParamsInterceptor(ParamsInterceptor interceptor) // 添加头部参数拦截器
addParamsInterceptor(ParamsInterceptor interceptor) // 添加普通参数拦截器
```

通过实现ParamsInterceptor来实现参数的拦截

```
public interface ParamsInterceptor {

    /**
     * 将请求所需参数添加至params中
     *
     * @param params 参数集合
     */
    void process(Map<String, Object> params);

    /**
     * 返回此参数拦截器即将添加的参数数量
     * 准确返回参数数量可以优化请求的参数构建
     * 可以默认为0
     *
     * @return 即将添加的参数数量
     */
    int getParamsSize();

}
```

#### 定制Retrofit对象

EasyHttp使用Retrofit来发起请求，每个请求都可以指定一个自定义的retrofit对象来发起请求。

```
EasyHttp.retrofit(Retrofit retrofit)
```

#### 重试选项

```
EasyHttp.from(TestResult.class)
				.setRetryOptions(RetryOptions.from(int count, int delay))
```

通过RetryOptions.from(int count, int delay)的方式来构建一个重试选项对象，它有两个参数：

1. count: 重试次数 
2. delay: 延迟多少ms后重试

#### 延迟请求选项

通过延迟请求选项，可以延迟发起请求：

```
setDelay(int delay)
```

delay为延迟的时长，单位为ms.

#### 文件下载选项

```
targetFile(File targetFile)
```

将下载的文件保存到targetFile中。

#### 同步请求选项

```d
sync()
```

通过添加sync()操作符，可以让一个请求变成同步请求：

```
EasyHttp.from(TestResult.class)
				.sync()
				.get();
```

#### 数据转换器

EasyHttp内部已经实现了默认的数据转换器，如果想自定义，可以通过以下接口实现：

```
setResultConverter(ResultConverter converter)
```

ResultConverter的定义如下，表示从原始数据original转为泛型类型T的实例对象：

```
public interface ResultConverter<T, V> {

    T convert(V original) throws Exception;

}
```

比如DefaultStringResultConverter：

```
public class DefaultStringResultConverter<T> implements ResultConverter<T, Response<String>> {
	...
}
```

从Response<String> 转换为泛型类型T。

#### 请求体拦截

```
setRequestBodyCreator(RequestBodyCreator creator)
```

通过创建一个RequestCreator来自定义RequestBody的创建，RequestCreator的定义如下：

```
public interface RequestBodyCreator {

    RequestBody createRequestBody(Map<String, String> params);

}
```

其中，params为最终完整的参数集合，根据params来构建最终的RequestBody对象，比如定义media type为json:

```
public class MyRequestBodyCreator implements RequestBodyCreator {
    String content = null;
    if (params != null && !params.isEmpty()) {
        content = mConverter.toJson(params);
        params.clear();
    }
    if (content == null) {
        content = "{}";
    }
    requestBody = RequestBody.create(content, MediaType.parse("application/json;charset=utf-8"));
}
```
