### In your plugin

`AnvilGUI.Builder`クラスは、AnvilGUIを構築するためのクラスである。
以下のメソッドにより、表示されたGUIの様々な部分を修正することができる。Javadocsは[こちら](http://docs.wesjd.net/AnvilGUI/)にあります。

#### `onClose(Consumer<Player>)`

`Consumer<Player>`の引数をとり、プレイヤーがアンビルGUIを閉じたときに呼び出される。

```java                                             
builder.onClose(player -> {                         
    player.sendMessage("You closed the inventory.");
});                                                 
``` 

#### `onComplete(Function<AnvilGUI.Completion, AnvilGUI.Response>)`

引数として `Function<AnvilGUI.Completion, List<AnvilGUI.ResponseAction>>` を取る。この関数は、プレイヤーが出力スロットをクリックしたときに呼び出される。
渡された `Completion` には、クリックしたプレイヤー、入力されたテキスト、左のアイテム、右のアイテム、出力が含まれます。List<AnvilGUI.ResponseAction>` を返す必要があります。
を含む可能性があります。

- インベントリーの閉鎖 (`AnvilGUI.ResponseAction.close()`)
- 入力テキストを置き換える (`AnvilGUI.ResponseAction.replaceInputText(String)`)
- 別のインベントリを開く (`AnvilGUI.ResponseAction.openInventory(Inventory)`)
- ジェネリックコードの実行 (`AnvilGUI.ResponseAction.close(Runnable)`)

アクションのリストは、供給された順番に実行されます。

```java                                                
builder.onComplete((completion) -> {                 
    if(completion.getText().equalsIgnoreCase("you")) {
        completion.getPlayer().sendMessage("You have magical powers!");
        return Arrays.asList(AnvilGUI.ResponseAction.close());              
    } else {                                           
        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Try again"));   
    }                                                  
});                                                    
```

#### `interactableSlots(int... slots)`

これは、ユーザーが用意されたアンビルスロットにアイテムを取り込む／入力することを許可／拒否するものです。
この機能は、アンビルGUIを使用して入力システムを作ろうとするときに便利です。

```java
builder.interactableSlots(Slot.INPUT_LEFT, Slot.INPUT_RIGHT);
```

#### `preventClose()`

AnvilGUIに、ユーザーがエスケープキーを押してインベントリを閉じるのを防ぐように指示します。
パスワード入力で再生するような場面で便利です。

```java                     
builder.preventClose();     
```                     

#### `text(String)`

リネームフィールドの初期テキストを設定するための `String` を受け取る。

```java                                           
builder.text("What is the meaning of life?");     
```  

#### `itemLeft(ItemStack)`

左の入力スロットに配置されるカスタム `ItemStack` を受け取る。

```java                                              
ItemStack stack = new ItemStack(Material.IRON_SWORD);
ItemMeta meta = stack.getItemMeta();                 
meta.setLore(Arrays.asList("Sharp iron sword"));             
stack.setItemMeta(meta); 
builder.itemLeft(stack);        
```         

#### `onLeftInputClick(Consumer<Player>)`

左の入力スロットのアイテムがクリックされたときに実行される `Consumer<Player>` を受け取ります。

```java                                              
builder.onLeftInputClick(player -> {
    player.sendMessage("You clicked the left input slot!");
});        
```      

#### `itemRight(ItemStack)`

右の入力スロットに配置されるカスタム `ItemStack` を受け取る。

```java                                              
ItemStack stack = new ItemStack(Material.IRON_INGOT);
ItemMeta meta = stack.getItemMeta();                 
meta.setLore(Arrays.asList("A piece of metal"));             
stack.setItemMeta(meta); 
builder.itemRight(stack);        
```         

#### `onRightInputClick(Consumer<Player>)`

右の入力スロットのアイテムがクリックされたときに実行される `Consumer<Player>` を受け取ります。

```java                                              
builder.onRightInputClick(player -> {
    player.sendMessage("You clicked the right input slot!");
});        
```

#### `title(String)`

インベントリのタイトルとして使用される `String` を受け取ります。Minecraft 1.14 以上で表示されます。

```java                            
builder.title("Enter your answer");
```                                

#### `plugin(Plugin)`

このanvil guiを作成している `Plugin` オブジェクトを取得する。これはリスナーを登録するために必要である。

```java                                         
builder.plugin(pluginInstance);                 
```                            

#### `open(Player)`

アンビルGUIを開くべき`Player`を受け取る。このメソッドは複数回呼び出すことができ、その際に
新しい `AnvilGUI.Builder` オブジェクトを作成する。

```java              
builder.open(player);
```                  

### A full example combining all methods

```java
new AnvilGUI.Builder()
	.onClose(player -> {                                               //インベントリ終了時に呼び出される
	player.sendMessage("You closed the inventory.");
	})
	.onComplete((completion) -> {                                    //インベントリ出力スロットがクリックされたときに呼び出されます。
	if(completion.getText().equalsIgnoreCase("you")) {
	completion.getPlayer().sendMessage("You have magical powers!");
	return Arrays.asList(AnvilGUI.ResponseAction.close());
	} else {
	return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Try again"));
	}
	})
	.preventClose()                                                    //インベントリが閉じられるのを防ぐ
	.interactableSlots(Slot.INPUT_RIGHT)                               //プレーヤーが右の入力項目を取り出し、交換できるようにする。
	.text("What is the meaning of life?")                              //GUI が開始するテキストを設定します。
	.itemLeft(new ItemStack(Material.IRON_SWORD))                      //1枠目にカスタムアイテムを使う
	.itemRight(new ItemStack(Material.IRON_SWORD))                     //2枠目にカスタムアイテムを使用する
	.onLeftInputClick(player -> player.sendMessage("first sword"))     //左の入力スロットがクリックされたときに呼び出される
	.onRightInputClick(player -> player.sendMessage("second sword"))   //右の入力スロットがクリックされたときに呼び出される
	.title("Enter your answer.")                                       //GUIのタイトルを設定する（1.14+でのみ動作）。
	.plugin(myPluginInstance)                                          //プラグインインスタンスを設定する
	.open(myPlayer);                                                   //提供されたプレーヤーのGUIを開く
```