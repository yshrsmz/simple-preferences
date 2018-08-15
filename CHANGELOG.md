## 2.0.2 - 2018/08/15

### Fix

- Fix dependency resolution failure


## 2.0.0 - 2018/08/14

### Breaking Changes

- Migrate to AndroidX. If you need to use legacy Android Support Libraries, use v1.4.0

### Enhancements

- Update to SDK 28


## 1.4.0 - 2017/03/07

### Enhancement

- Add `Preferenecs.expose` option to control visibility of the generated class([issue #10](https://github.com/yshrsmz/simple-preferences/issues/10)). 
  The default value is `true` for backward compatibility. If you want a generated class to be package private, you need to manually set to `false`.


## 1.3.1 - 2016/08/08

### Enhancement

- Add clear method using `SharedPreferences.Editor#commit()`(which I forgot to add in previous release). You need to enable it with `@Preferences(needCommitMethodForClear = true)`


## 1.3.0 - 2016/08/03

### Enhancements

- Setter/Remover method with `SharedPreferences.Editor#commit()` is now available. You need to enable it with `@Key(needCommitMethod = true)`
- Add overload method for Getter that you can pass default value as parameter


## 1.2.0 - 2016/03/12

### Enhancements

- Setter methods now return it's class instance(now we can chain!)
- Update to support library 23.2.1


## 1.1.0

initial release

