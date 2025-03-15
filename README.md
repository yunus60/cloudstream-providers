# CloudStream Eklentileri

Bu repo, [CloudStream](https://github.com/recloudstream/cloudstream) uygulaması için geliştirilmiş Türkçe içerik sağlayıcı eklentileri içerir.

## Eklentiler

| Eklenti | Açıklama |
|---------|----------|
| [DDizi](./DDizi) | [DDizi.im](https://www.ddizi.im) sitesinden dizi içeriklerine erişim sağlar |

## Kurulum

1. CloudStream uygulamasını açın
2. Ayarlar > Eklentiler > Depo Ekle bölümüne gidin
3. Depo URL'si olarak `https://github.com/yunus60/cloudstream-providers` ekleyin
4. Eklentiler listesinden istediğiniz eklentiyi bulun ve yükleyin

## Geliştirme

Eklenti geliştirmek için:

1. Repo'yu klonlayın:
```
git clone https://github.com/yunus60/cloudstream-providers.git
```

2. Android Studio ile açın ve geliştirmeye başlayın

3. Eklentiyi derlemek için:
```
./gradlew :EklentiAdi:make
```

4. Eklentiyi test etmek için:
```
./gradlew :EklentiAdi:deployWithAdb
```

## Hata Ayıklama

Logcat ile hata ayıklamak için:
```
adb logcat -v brief | grep "{Eklenti Adı}"
```

## Katkıda Bulunma

1. Repo'yu fork edin
2. Yeni bir branch oluşturun (`git checkout -b feature/yeni-eklenti`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni eklenti: XYZ'`)
4. Branch'inizi push edin (`git push origin feature/yeni-eklenti`)
5. Pull Request oluşturun

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## İletişim

Geliştirici: [yunus60](https://github.com/yunus60)

## Teşekkürler

- CloudStream geliştiricilerine
- Tüm katkıda bulunanlara
