from telegram import Update
from telegram.ext import Updater, CommandHandler, MessageHandler, Filters, CallbackContext,CallbackQueryHandler
from telegram import InlineKeyboardMarkup, InlineKeyboardButton

import configparser
import logging
import re
import load_database
import verify

user_docs = {}
user_foodname = {}


def main():
    # Load your token and create an Updater for your Bot
    
    config = configparser.ConfigParser()
    config.read('config.ini')
    updater = Updater(token=(config['TELEGRAM']['ACCESS_TOKEN']), use_context=True)
    dispatcher = updater.dispatcher

    # You can set this logging module, so you will know when and why things do not work as expected
    logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                        level=logging.INFO)
    

    # on different commands - answer in Telegram
    dispatcher.add_handler(CommandHandler("start", start))
    dispatcher.add_handler(CommandHandler("recommend", recommend))
    dispatcher.add_handler(CommandHandler("help", help_command))
    dispatcher.add_handler(CommandHandler("addRecipe", addRecipe))
    dispatcher.add_handler(CommandHandler("searchOnline", searchOnline))
    updater.dispatcher.add_handler(CallbackQueryHandler(feedback))



    # To start the bot:
    updater.start_polling()
    updater.idle()


def start(update, context):
    update.message.reply_text("Hello! I'm a food recipe chatbot!")
    update.message.reply_text("Here's the user guide. You can try our commands by using the ones below \n /recommend \n /addRecipe \n /searchOnline")


# Define a few command handlers. These usually take the two arguments update and
# context. Error handlers also receive the raised TelegramError object in error.
def help_command(update: Update, context: CallbackContext) -> None:
    """Send a message when the command /help is issued."""
    update.message.reply_text("Here's the user guide. You can try our commands by using the ones below \n /recommend \n /addRecipe \n /searchOnline")

def recommend(update: Update, context: CallbackContext) -> None:
    """Send a message when the command /recommend is issued."""
    try: 
        global user_docs
        logging.info(context.args[0])
        msg = context.args[0].capitalize() 
        res = load_database.getCookingdata(msg)
        if res==False:
            update.message.reply_text("There's no such recipe in our database. How about you show us something using /addRecipe ?")
        else:
            chat_id = update.message.chat.id
            tmp_docs = []
            for doc in res:
                tmp = doc.to_dict()
                tmp_docs.append(tmp)
            
            tmp_docs = load_database.sortUrl(tmp_docs)
            user_docs[chat_id] = tmp_docs
            update.message.reply_text(user_docs[chat_id][0]["url"],
                reply_markup = InlineKeyboardMarkup([[
                    InlineKeyboardButton('I like it', callback_data = 'liked'),
                    InlineKeyboardButton('This one no good', callback_data = 'disliked')]]))    

    except (IndexError, ValueError):
        update.message.reply_text('Usage: /recommend <foodname>')

def addRecipe(update: Update, context: CallbackContext) -> None:
    try: 
        document_name = context.args[0].capitalize()
        url = context.args[1]

        if re.match(r'^https?:/{2}\w.+$', url):
            if verify.verifyURL(url,document_name):
                if verify.verifyExists(url,document_name)==False:
                    load_database.setCookingdata(document_name,url)
                    update.message.reply_text('Thank you for your contribution !')
                elif verify.verifyExists(url,document_name)==True:
                    update.message.reply_text('The URL exists in our database ! Please try another one !')
            else:
                update.message.reply_text('Please enter a correct URL that matches your recipe name !')
        else:    
            update.message.reply_text('Please enter a correct URL format !')
    except (IndexError, ValueError):
        update.message.reply_text('Usage: /addRecipe <foodname> <url>')

def searchOnline(update: Update, context: CallbackContext) -> None:
    """Send a message when the command /searchOnline is issued."""
    try: 
        global user_foodname
        logging.info(context.args[0])
        chat_id = update.message.chat.id
        user_dict = {}
        foodname = context.args[0].capitalize() 
        url = verify.searchURL(foodname)
        user_dict['foodname'] = foodname
        user_dict['url']= url
        user_foodname[chat_id] = user_dict

        update.message.reply_text(user_foodname[chat_id]['url'],
                reply_markup = InlineKeyboardMarkup([[
                    InlineKeyboardButton('This should been seen by someone else!', callback_data = 'save'),
                    InlineKeyboardButton('Gimme something else', callback_data = 'retry')]])) 
        
    except (IndexError, ValueError):
        update.message.reply_text('Usage: /searchOnline <foodname>')

def feedback(update: Update, context: CallbackContext) -> None:
    global user_docs
    global user_foodname

    chat_id =  update.callback_query.message.chat.id
    res = update.callback_query.data
    if (res=='liked'):
        load_database.updateLiked(user_docs[chat_id][0]['type'],user_docs[chat_id][0]['id'])
        update.callback_query.edit_message_text(user_docs[chat_id][0]["url"])
        update.callback_query.message.reply_text("Glad You Liked It !")
    elif (res=="disliked"):
        if len(user_docs[chat_id])>1:
            del(user_docs[chat_id][0])
            update.callback_query.edit_message_text(user_docs[chat_id][0]["url"],
                reply_markup = InlineKeyboardMarkup([[
                    InlineKeyboardButton('I like it', callback_data = 'liked'),
                    InlineKeyboardButton('This one no good', callback_data = 'disliked')]]))
        else:
            update.callback_query.edit_message_text(user_docs[chat_id][0]["url"])
            update.callback_query.message.reply_text("No more recommends, you're too picky! How about you show us something using /addRecipe ?")
    elif(res=="save"):
        if verify.verifyExists(user_foodname[chat_id]['url'],user_foodname[chat_id]['foodname'])==False:
                load_database.setCookingdata(user_foodname[chat_id]['foodname'],user_foodname[chat_id]['url'])
                update.callback_query.edit_message_text(user_foodname[chat_id]['url'])
        update.callback_query.message.reply_text("Glad You Liked It !")
    elif(res=="retry"):
        user_foodname[chat_id]['url'] = verify.searchURL(user_foodname[chat_id]['foodname'])
        update.callback_query.edit_message_text(user_foodname[chat_id]['url'],
                reply_markup = InlineKeyboardMarkup([[
                    InlineKeyboardButton('This should been seen by someone else!', callback_data = 'save'),
                    InlineKeyboardButton('Gimme something else', callback_data = 'retry')]]))

    return


if __name__ == '__main__':
    main()