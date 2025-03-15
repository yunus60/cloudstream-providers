# DDizi

DDizi, CloudStream uygulaması için geliştirilmiş bir eklentidir. Bu eklenti, [DDizi.im](https://www.ddizi.im) sitesindeki dizi içeriklerine erişmenizi sağlar.

## Özellikler

- Yeni eklenen bölümleri görüntüleme
- Yabancı dizileri listeleme
- Eski dizileri görüntüleme
- Dizi arama
- Sezon ve bölüm bilgilerini otomatik algılama
- Yüksek kaliteli video akışı

## Kurulum

1. CloudStream uygulamasını açın
2. Ayarlar > Eklentiler > Depo Ekle bölümüne gidin
3. Depo URL'si olarak `https://github.com/yunus60/cloudstream-providers` ekleyin
4. Eklentiler listesinden "DDizi" eklentisini bulun ve yükleyin

## Kullanım

Eklenti yüklendikten sonra, CloudStream ana sayfasında "DDizi" kaynağını seçerek kullanmaya başlayabilirsiniz. Ana sayfada şu kategoriler bulunmaktadır:

- Yeni Eklenen Bölümler
- Yabancı Diziler
- Eski Diziler

Ayrıca, arama özelliğini kullanarak istediğiniz diziyi bulabilirsiniz.

## Sorun Giderme

Eğer video oynatma sırasında sorun yaşıyorsanız:

1. Uygulamayı güncelleyin
2. Eklentiyi yeniden yükleyin
3. Farklı bir video kaynağı seçin
4. İnternet bağlantınızı kontrol edin

## Geliştirici Notları

Bu eklenti, DDizi.im sitesinin HTML yapısına göre tasarlanmıştır. Site yapısında değişiklik olması durumunda eklenti güncellenecektir.

Hata ayıklama için logcat kullanabilirsiniz:
```
adb logcat -v brief | grep "DDizi:"
```

Eklentiyi derlemek için:
```
./gradlew :DDizi:make
```

Eklentiyi test etmek için:
```
./gradlew :DDizi:deployWithAdb
```

## Lisans

Bu eklenti açık kaynak kodludur ve MIT lisansı altında dağıtılmaktadır.

## İletişim

Geliştirici: [yunus60](https://github.com/yunus60)

Sorunlar ve öneriler için GitHub üzerinden issue açabilirsiniz: [https://github.com/yunus60/cloudstream-providers/issues](https://github.com/yunus60/cloudstream-providers/issues)

## Teşekkürler

- CloudStream geliştiricilerine
- DDizi.im ekibine
- Tüm katkıda bulunanlara