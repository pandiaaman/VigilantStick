public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.my_location :
                i = new Intent(this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.my_tts :
                i = new Intent(this,TTS.class);
                startActivity(i);
                break;
            case R.id.my_stt :
                i = new Intent(this, STT.class);
                startActivity(i);
                break;
            case R.id.my_nav :
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }, 1000);
                break;
            case R.id.my_assistant :
                //i = new Intent(this,MyAssistant.class);
                //startActivity(i);
                startActivity(new Intent(Intent.ACTION_VOICE_COMMAND)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.control :
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                mySpeechRecognizer.startListening(intent);
                break;
            default:break;
        }
    }

//Commands
private void processResult(String command) {
        command = command.toLowerCase();
        if(command.indexOf("what") != -1) {
            // what is your name?
            if(command.indexOf("your name") != -1) {
                speak("My name is Nemo.");
            }
            //what is the time?
            if(command.indexOf("time") != -1) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(),
                        DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is " + time);
            }
        }
        if(command.indexOf("open") != -1) {
            // open browser
            if(command.indexOf("browser") != -1) {
                //speak("Gaand Maraao");
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        //Uri.parse("https://tutsplus.com"));
                          //Uri.parse("192.168.1.11/phpmyadmin"));
                        Uri.parse("https://192.168.1.11/phpmyadmin"));
                startActivity(intent);
            }
            // open the map
            if(command.indexOf("map") != -1) {
               // speak("Sorry");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }, 1000);
            }
            // open google assistant
            if(command.indexOf("assistant") != -1) {
                startActivity(new Intent(Intent.ACTION_VOICE_COMMAND)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            // open a Test Activity
            if(command.indexOf("test") != -1) {
                Intent intent = new Intent(VigActivity.this, TTS.class);
                startActivity(intent);
            }
        }
        if(command.indexOf("send") != -1) {
            // open google assistant
            if(command.indexOf("message") != -1) {
                //to send text in whatsapp
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(sendIntent, ""));
                startActivity(sendIntent);*/
                // open message app
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                startActivity(intent);
            }
        }
    }
