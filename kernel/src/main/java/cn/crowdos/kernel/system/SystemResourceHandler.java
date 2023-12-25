package cn.crowdos.kernel.system;

public interface SystemResourceHandler<T> {
    T getResourceView();
    T getResource();
}
