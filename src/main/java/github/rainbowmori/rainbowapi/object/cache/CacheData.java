package github.rainbowmori.rainbowapi.object.cache;

public interface CacheData<T> {

  /**
   * {@link CacheManager} の キーの値を返します
   * @return 保存されているcacheのキーの値を返します
   */
  T getKey();

}
