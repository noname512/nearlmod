package nearlmod.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.ChangeImgAction;

public class CandleKnight extends AbstractMonster {
    public static final String ID = "nearlmod:CandleKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/candleknight.png";
    public static final String CHARGING_IMAGE = "images/monsters/candleknight_charging.png";
    private int candleDmg;
    private int flameDmg;
    private int swordDmg;
    private int said = 0;

    public CandleKnight() {
        super(NAME, ID, 100, 0, 0, 150.0F, 320.0F, IMAGE);
        this.type = EnemyType.ELITE;
        candleDmg = 10;
        flameDmg = 30;
        swordDmg = 6;
        this.damage.add(new DamageInfo(this, candleDmg));
        this.damage.add(new DamageInfo(this, flameDmg));
        this.damage.add(new DamageInfo(this, swordDmg));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(AbstractDungeon.player, DIALOG[2], 0.3F, 3.0F));
    }

    public void changeImg(String imgUrl) {
        this.img = new Texture(imgUrl);
    }

    @Override
    public void takeTurn() {
        if (!this.isDying && this.currentHealth <= this.maxHealth * 0.3F && this.said == 1) {
            this.said = 2;
            addToBot(new TalkAction(AbstractDungeon.player, DIALOG[5], 0.3F, 3.0F));
            addToBot(new TalkAction(this, DIALOG[6], 0.3F, 3.0F));
        }
        if (!this.isDying && this.currentHealth <= this.maxHealth * 0.6F && this.said == 0) {
            this.said = 1;
            addToBot(new TalkAction(this, DIALOG[3], 0.3F, 3.0F));
            addToBot(new TalkAction(AbstractDungeon.player, DIALOG[4], 0.3F, 3.0F));
        }
        if (this.nextMove == 2) {
            setMove(MOVES[1], (byte) 3, Intent.ATTACK, this.damage.get(1).base);
        } else {
            if (this.nextMove == 1) {
                addToBot(new TalkAction(this, DIALOG[0], 3.0F, 3.0F));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                addToBot(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
            } else if (this.nextMove == 4) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            } else if (this.nextMove == 3) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                addToBot(new ChangeImgAction(this, IMAGE));
            } else if (this.nextMove == 5) {
                for (int i = 1; i <= 2; i++)
                    addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
            }
            if (this.nextMove == 1 || AbstractDungeon.aiRng.random(0, 4) == 0) {
                addToBot(new ChangeImgAction(this, CHARGING_IMAGE));
                setMove(MOVES[0], (byte) 2, Intent.UNKNOWN);
                return;
            }
            int randInt = AbstractDungeon.aiRng.random(0, 1);
            if (randInt == 0)
                setMove((byte)4, Intent.ATTACK, this.damage.get(0).base);
            else
                setMove((byte)5, Intent.ATTACK, this.damage.get(2).base, 2, true);
        }
    }

    @Override
    public void die() {
        // TODO 如果要死亡前说话可能需要额外写一个dieAction，保证super.die()在对话完成后再执行
        addToBot(new TalkAction(this, DIALOG[7], 0.3F, 3.0F));
        super.die();
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }
}
